package com.concordium.sdk.crypto.wallet;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.concordium.sdk.crypto.bls.BLSSecretKey;

public class IdentityTest {

    private static String TEST_SEED = "efa5e27326f8fa0902e647b52449bf335b7b605adc387015ec903f41d95080eb71361cbc7fb78721dcd4f3926a337340aa1406df83332c44c1cdcfe100603860";

    @Test
    public void testCreatingIdentityRequest() throws Exception {
        ConcordiumHdWallet wallet = ConcordiumHdWallet.fromHex(TEST_SEED, Network.TESTNET);
        BLSSecretKey idCredSec = wallet.getIdCredSec(0, 0);
        BLSSecretKey prfKey = wallet.getPrfKey(0, 0);
        String blindingRandomness = wallet.getSignatureBlindingRandomness(0, 0);

        IdentityRequestInput input = IdentityRequestInput.builder()
                .globalContext(FileHelpers.getCryptographicParameters())
                .ipInfo(FileHelpers.getIdentityProviderInfo())
                .arsInfos(FileHelpers.getAnonymityRevokerInfos())
                .arThreshold(2)
                .idCredSec(idCredSec)
                .prfKey(prfKey)
                .blindingRandomness(blindingRandomness)
                .build();

        String result = Identity.createIdentityRequest(input);

        assertTrue(result.contains(
                "\"idCredPub\":\"b23e360b21cb8baad1fb1f9a593d1115fc678cb9b7c1a5b5631f82e088092d79d34b6a6c8520c06c41002a666adf792f\""));
    }

    @Test
    public void testCreatingIdentityRecoveryRequest() throws Exception {
        IdentityRecoveryRequestInput input = IdentityRecoveryRequestInput.builder()
                .globalContext(FileHelpers.getCryptographicParameters()).ipInfo(FileHelpers.getIdentityProviderInfo()).timestamp(0)
                .idCredSec(BLSSecretKey.from("7392eb0b4840c8a6f9314e99a8dd3e2c3663a1e615d8820851e3abd2965fab18"))
                .build();

        String result = Identity.createIdentityRecoveryRequest(input);

        assertTrue(result.contains(
                "\"idCredPub\":\"b23e360b21cb8baad1fb1f9a593d1115fc678cb9b7c1a5b5631f82e088092d79d34b6a6c8520c06c41002a666adf792f\""));
    }
}
