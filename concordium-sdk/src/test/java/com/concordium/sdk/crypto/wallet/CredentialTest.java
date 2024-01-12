package com.concordium.sdk.crypto.wallet;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.TLSConfig;
import com.concordium.sdk.crypto.bls.BLSSecretKey;
import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.crypto.wallet.credential.CredentialDeploymentDetails;
import com.concordium.sdk.crypto.wallet.credential.CredentialDeploymentSerializationContext;
import com.concordium.sdk.crypto.wallet.credential.UnsignedCredentialDeploymentInfo;
import com.concordium.sdk.crypto.wallet.credential.UnsignedCredentialDeploymentInfoWithRandomness;
import com.concordium.sdk.crypto.wallet.identityobject.IdentityObject;
import com.concordium.sdk.responses.accountinfo.credential.AttributeType;
import com.concordium.sdk.responses.blocksummary.updates.queues.AnonymityRevokerInfo;
import com.concordium.sdk.responses.blocksummary.updates.queues.IdentityProviderInfo;
import com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters;
import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.CredentialPublicKeys;
import com.concordium.sdk.transactions.Index;
import com.concordium.sdk.transactions.TransactionExpiry;
import com.concordium.sdk.types.UInt64;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;


public class CredentialTest {
    
    static String readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    private CryptographicParameters getCryptographicParameters() throws Exception {
        return JsonMapper.INSTANCE.readValue(
                readFile("./src/test/testresources/wallet/global_test.json", Charset.forName("UTF-8")),
                CryptographicParameters.class);
    }

    private IdentityProviderInfo getIdentityProviderInfo() throws Exception {
        return JsonMapper.INSTANCE.readValue(
                readFile("./src/test/testresources/wallet/ip_info_test.json", Charset.forName("UTF-8")),
                IdentityProviderInfo.class);
    }

    private Map<String, AnonymityRevokerInfo> getAnonymityRevokerInfos() throws Exception {
        MapType mapType = TypeFactory.defaultInstance().constructMapType(Map.class, String.class,
                AnonymityRevokerInfo.class);
        return JsonMapper.INSTANCE.readValue(
                readFile("./src/test/testresources/wallet/ars_infos_test.json", Charset.forName("UTF-8")), mapType);
    }

    private IdentityObject getIdentityObject() throws Exception {
    return JsonMapper.INSTANCE.readValue(
                readFile("./src/test/testresources/wallet/id_object_test.json", Charset.forName("UTF-8")),
                IdentityObject.class);    }

    // private static String TEST_SEED = "efa5e27326f8fa0902e647b52449bf335b7b605adc387015ec903f41d95080eb71361cbc7fb78721dcd4f3926a337340aa1406df83332c44c1cdcfe100603860";
    private static String TEST_SEED = "8ca871b924afd59f2db6e7d91bb5751f56f8b73561a768e103038f84e731e3c810b52bffea10cb578a1ee01c67c6b95d7ef38a118ee3326997d24a490d2eb211";

    @Test
    public void createUnsignedCredential() throws Exception {
        ConcordiumHdWallet wallet = ConcordiumHdWallet.fromHex(TEST_SEED, Network.Mainnet);

        CredentialPublicKeys credentialPublicKeys = CredentialPublicKeys.from(Collections.singletonMap(Index.from(0), wallet.getAccountPublicKey(0, 0, 0)), 1);
        Map<AttributeType, String> attributeRandomness = new HashMap<>();
        for (AttributeType attrType : AttributeType.values()) {
            attributeRandomness.put(attrType, wallet.getAttributeCommitmentRandomness(0, 0, 0, attrType.ordinal()));
        }

        UnsignedCredentialInput input = UnsignedCredentialInput.builder()
            .ipInfo(getIdentityProviderInfo())
            .globalContext(getCryptographicParameters())
            .arsInfos(getAnonymityRevokerInfos())
            .idObject(getIdentityObject())
            .credNumber(0)
            .attributeRandomness(attributeRandomness)
            .blindingRandomness(wallet.getSignatureBlindingRandomness(0, 0))
            .credentialPublicKeys(credentialPublicKeys)
            .idCredSec(BLSSecretKey.from(wallet.getIdCredSec(0, 0)))
            .prfKey(BLSSecretKey.from(wallet.getPrfKey(0, 0)))
            .revealedAttributes(Collections.emptyList())
            .build();

        UnsignedCredentialDeploymentInfoWithRandomness result = Credential.createUnsignedCredential(input);

        TransactionExpiry expiry = TransactionExpiry.fromLong(1705077126998l);

        byte[] credentialDeploymentSignDigest = Credential.getCredentialDeploymentSignDigest(new CredentialDeploymentDetails(result.getUnsignedCdi(), expiry));
        ED25519SecretKey signingKey = wallet.getAccountSigningKey(0, 0, 0);
        byte[] signature = signingKey.sign(credentialDeploymentSignDigest);
        
        assertTrue(credentialPublicKeys.getKeys().get(Index.from(0)).verify(credentialDeploymentSignDigest, signature));

        System.out.println(Hex.encodeHexString(signature));
        CredentialDeploymentSerializationContext context = new CredentialDeploymentSerializationContext(result.getUnsignedCdi(), Collections.singletonList(Hex.encodeHexString(signature)));
        byte[] credentialPayload = Credential.serializeCredentialDeploymentPayload(context);

        Connection connection = Connection.builder().timeout(10000).host("grpc.testnet.concordium.com").port(20000).useTLS(TLSConfig.auto()).build();
        ClientV2 client = ClientV2.from(connection);


        client.sendCredentialDeploymentTransaction(expiry, credentialPayload);
    }
} 
