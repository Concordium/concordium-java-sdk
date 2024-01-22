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

import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.crypto.wallet.credential.CredentialDeploymentDetails;
import com.concordium.sdk.crypto.wallet.credential.CredentialDeploymentSerializationContext;
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
                readFile("./src/test/testresources/wallet/global.json", Charset.forName("UTF-8")),
                CryptographicParameters.class);
    }

    private IdentityProviderInfo getIdentityProviderInfo() throws Exception {
        return JsonMapper.INSTANCE.readValue(
                readFile("./src/test/testresources/wallet/ip_info.json", Charset.forName("UTF-8")),
                IdentityProviderInfo.class);
    }

    private Map<String, AnonymityRevokerInfo> getAnonymityRevokerInfos() throws Exception {
        MapType mapType = TypeFactory.defaultInstance().constructMapType(Map.class, String.class,
                AnonymityRevokerInfo.class);
        return JsonMapper.INSTANCE.readValue(
                readFile("./src/test/testresources/wallet/ars_infos.json", Charset.forName("UTF-8")), mapType);
    }

    private IdentityObject getIdentityObject() throws Exception {
    return JsonMapper.INSTANCE.readValue(
                readFile("./src/test/testresources/wallet/id_object.json", Charset.forName("UTF-8")),
                IdentityObject.class);    }

    private static String TEST_SEED = "efa5e27326f8fa0902e647b52449bf335b7b605adc387015ec903f41d95080eb71361cbc7fb78721dcd4f3926a337340aa1406df83332c44c1cdcfe100603860";

    @Test
    public void createUnsignedCredential() throws Exception {
        ConcordiumHdWallet wallet = ConcordiumHdWallet.fromHex(TEST_SEED, Network.TESTNET);
        int credentialCounter = 3;

        CredentialPublicKeys credentialPublicKeys = CredentialPublicKeys.from(Collections.singletonMap(Index.from(0), wallet.getAccountPublicKey(0, 0, credentialCounter)), 1);
        Map<AttributeType, String> attributeRandomness = new HashMap<>();
        for (AttributeType attrType : AttributeType.values()) {
            attributeRandomness.put(attrType, wallet.getAttributeCommitmentRandomness(0, 0, credentialCounter, attrType.ordinal()));
        }

        UnsignedCredentialInput input = UnsignedCredentialInput.builder()
            .ipInfo(getIdentityProviderInfo())
            .globalContext(getCryptographicParameters())
            .arsInfos(getAnonymityRevokerInfos())
            .idObject(getIdentityObject())
            .credNumber(credentialCounter)
            .attributeRandomness(attributeRandomness)
            .blindingRandomness(wallet.getSignatureBlindingRandomness(0, 0))
            .credentialPublicKeys(credentialPublicKeys)
            .idCredSec(wallet.getIdCredSec(0, 0))
            .prfKey(wallet.getPrfKey(0, 0))
            .revealedAttributes(Collections.emptyList())
            .build();

        UnsignedCredentialDeploymentInfoWithRandomness result = Credential.createUnsignedCredential(input);
        TransactionExpiry expiry = TransactionExpiry.fromLong(new Date().getTime() + 360);

        byte[] credentialDeploymentSignDigest = Credential.getCredentialDeploymentSignDigest(new CredentialDeploymentDetails(result.getUnsignedCdi(), expiry));
        ED25519SecretKey signingKey = wallet.getAccountSigningKey(0, 0, credentialCounter);
        byte[] signature = signingKey.sign(credentialDeploymentSignDigest);
        
        assertTrue(credentialPublicKeys.getKeys().get(Index.from(0)).verify(credentialDeploymentSignDigest, signature));

        CredentialDeploymentSerializationContext context = new CredentialDeploymentSerializationContext(result.getUnsignedCdi(), Collections.singletonMap(Index.from(0), Hex.encodeHexString(signature)));
        Credential.serializeCredentialDeploymentPayload(context);
    }
} 
