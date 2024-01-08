package com.concordium.sdk.crypto.wallet;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import org.junit.Test;

import com.concordium.sdk.responses.blocksummary.updates.queues.AnonymityRevokerInfo;
import com.concordium.sdk.responses.blocksummary.updates.queues.IdentityProviderInfo;
import com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters;
import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.types.UInt32;
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
        return JsonMapper.INSTANCE.readValue(readFile("./src/test/testresources/wallet/global.json", Charset.forName("UTF-8")), CryptographicParameters.class);
    }

    private IdentityProviderInfo gIdentityProviderInfo() throws Exception {
        return JsonMapper.INSTANCE.readValue(readFile("./src/test/testresources/wallet/ip_info.json", Charset.forName("UTF-8")), IdentityProviderInfo.class);
    }

    private Map<String, AnonymityRevokerInfo> getAnonymityRevokerInfos() throws Exception {
        MapType mapType = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, AnonymityRevokerInfo.class);
        return JsonMapper.INSTANCE.readValue(readFile("./src/test/testresources/wallet/ars_infos.json", Charset.forName("UTF-8")), mapType);
    }

    @Test
    public void testCreatingIdentityRequest() throws Exception {
        IdentityRequestInput input = new IdentityRequestInput();

        IdentityRequestCommon common = new IdentityRequestCommon();
        common.setGlobalContext(getCryptographicParameters());
        common.setIpInfo(gIdentityProviderInfo());
        common.setArsInfos(getAnonymityRevokerInfos());
        common.setArThreshold(2);
        input.setCommon(common);

        ConcordiumHdWallet wallet = ConcordiumHdWallet.fromHex(TEST_SEED, Network.Testnet);
        String idCredSec = wallet.getIdCredSec(0, 0);
        String prfKey = wallet.getPrfKey(0, 0);
        String blindingRandomness = wallet.getSignatureBlindingRandomness(0, 0);
    
        input.setIdCredSec(idCredSec);
        input.setPrfKey(prfKey);
        input.setBlindingRandomness(blindingRandomness);

        UInt32 test = UInt32.from(51);
        System.out.println(test);

        Map<String, AnonymityRevokerInfo> infos = getAnonymityRevokerInfos();
        System.out.println(JsonMapper.INSTANCE.writeValueAsString(infos));




        String result = Identity.createIdentityRequest(input);

    

    
    }

}
