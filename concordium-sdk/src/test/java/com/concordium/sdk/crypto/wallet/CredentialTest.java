package com.concordium.sdk.crypto.wallet;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
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
import com.concordium.sdk.transactions.Expiry;
import com.concordium.sdk.transactions.Index;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;


public class CredentialTest {
    
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
            .ipInfo(FileHelpers.getIdentityProviderInfo())
            .globalContext(FileHelpers.getCryptographicParameters())
            .arsInfos(FileHelpers.getAnonymityRevokerInfos())
            .idObject(FileHelpers.getIdentityObject())
            .credNumber(credentialCounter)
            .attributeRandomness(attributeRandomness)
            .blindingRandomness(wallet.getSignatureBlindingRandomness(0, 0))
            .credentialPublicKeys(credentialPublicKeys)
            .idCredSec(wallet.getIdCredSec(0, 0))
            .prfKey(wallet.getPrfKey(0, 0))
            .revealedAttributes(Collections.emptyList())
            .build();

        UnsignedCredentialDeploymentInfoWithRandomness result = Credential.createUnsignedCredential(input);
        Expiry expiry = Expiry.createNew().addMinutes(5);

        byte[] credentialDeploymentSignDigest = Credential.getCredentialDeploymentSignDigest(new CredentialDeploymentDetails(result.getUnsignedCdi(), expiry));
        ED25519SecretKey signingKey = wallet.getAccountSigningKey(0, 0, credentialCounter);
        byte[] signature = signingKey.sign(credentialDeploymentSignDigest);
        
        assertTrue(credentialPublicKeys.getKeys().get(Index.from(0)).verify(credentialDeploymentSignDigest, signature));

        CredentialDeploymentSerializationContext context = new CredentialDeploymentSerializationContext(result.getUnsignedCdi(), Collections.singletonMap(Index.from(0), Hex.encodeHexString(signature)));
        Credential.serializeCredentialDeploymentPayload(context);
    }
} 
