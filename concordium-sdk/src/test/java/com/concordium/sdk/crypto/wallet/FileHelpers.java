package com.concordium.sdk.crypto.wallet;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import com.concordium.sdk.crypto.wallet.identityobject.IdentityObject;
import com.concordium.sdk.responses.blocksummary.updates.queues.AnonymityRevokerInfo;
import com.concordium.sdk.responses.blocksummary.updates.queues.IdentityProviderInfo;
import com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters;
import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class FileHelpers {
    public static String readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
    
    public static CryptographicParameters getCryptographicParameters() throws Exception {
        return JsonMapper.INSTANCE.readValue(
                readFile("./src/test/testresources/wallet/global.json", Charset.forName("UTF-8")),
                CryptographicParameters.class);
    }

    public static IdentityProviderInfo getIdentityProviderInfo() throws Exception {
        return JsonMapper.INSTANCE.readValue(
                readFile("./src/test/testresources/wallet/ip_info.json", Charset.forName("UTF-8")),
                IdentityProviderInfo.class);
    }

    public static Map<String, AnonymityRevokerInfo> getAnonymityRevokerInfos() throws Exception {
        MapType mapType = TypeFactory.defaultInstance().constructMapType(Map.class, String.class,
                AnonymityRevokerInfo.class);
        return JsonMapper.INSTANCE.readValue(
                readFile("./src/test/testresources/wallet/ars_infos.json", Charset.forName("UTF-8")), mapType);
    }

    public static IdentityObject getIdentityObject() throws Exception {
    return JsonMapper.INSTANCE.readValue(
                readFile("./src/test/testresources/wallet/id_object.json", Charset.forName("UTF-8")),
                IdentityObject.class);    }

}
