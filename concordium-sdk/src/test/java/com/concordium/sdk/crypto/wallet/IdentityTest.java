package com.concordium.sdk.crypto.wallet;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import org.junit.Test;

import com.concordium.sdk.crypto.bls.BLSSecretKey;
import com.concordium.sdk.responses.blocksummary.updates.queues.AnonymityRevokerInfo;
import com.concordium.sdk.responses.blocksummary.updates.queues.IdentityProviderInfo;
import com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters;
import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class IdentityTest {

    private static String TEST_SEED = "efa5e27326f8fa0902e647b52449bf335b7b605adc387015ec903f41d95080eb71361cbc7fb78721dcd4f3926a337340aa1406df83332c44c1cdcfe100603860";

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

    @Test
    public void testCreatingIdentityRequest() throws Exception {
        ConcordiumHdWallet wallet = ConcordiumHdWallet.fromHex(TEST_SEED, Network.Testnet);
        String idCredSec = wallet.getIdCredSec(0, 0);
        String prfKey = wallet.getPrfKey(0, 0);
        String blindingRandomness = wallet.getSignatureBlindingRandomness(0, 0);

        IdentityRequestInput input = IdentityRequestInput.builder()
                .globalContext(getCryptographicParameters())
                .ipInfo(getIdentityProviderInfo())
                .arsInfos(getAnonymityRevokerInfos())
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
                .globalContext(getCryptographicParameters()).ipInfo(getIdentityProviderInfo()).timestamp(0)
                .idCredSec(BLSSecretKey.from("7392eb0b4840c8a6f9314e99a8dd3e2c3663a1e615d8820851e3abd2965fab18")).build();

        String result = Identity.createIdentityRecoveryRequest(input);

        assertTrue(result.contains("\"idCredPub\":\"b23e360b21cb8baad1fb1f9a593d1115fc678cb9b7c1a5b5631f82e088092d79d34b6a6c8520c06c41002a666adf792f\""));
    }
}
