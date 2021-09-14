package com.concordium.sdk.responsetypes.accountinfo;

import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.responsetypes.accountinfo.credential.Credential;
import lombok.*;

import java.util.Map;

/**
 * AccountInfo
 */
@Getter
@Setter
@ToString
public class AccountInfo {
    private int accountNonce;
    private String accountAmount;
    private int accountThreshold;
    private String accountEncryptionKey;
    private int accountIndex;
    private Baker accountBaker;
    private EncryptedAmount accountEncryptedAmount;
    private ReleaseSchedule accountReleaseSchedule;
    private Map<String, Credential> accountCredentials;

    @SneakyThrows
    public static AccountInfo fromJson(String accountInfoJsonString) {
        return JsonMapper.MAPPER.readValue(accountInfoJsonString, AccountInfo.class);
    }
}

