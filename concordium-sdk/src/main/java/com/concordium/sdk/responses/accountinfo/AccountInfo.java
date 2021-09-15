package com.concordium.sdk.responses.accountinfo;

import com.concordium.sdk.responses.accountinfo.credential.Credential;
import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

/**
 * AccountInfo
 */
@Getter
@ToString
public final class AccountInfo {
    private final int accountNonce;
    private final String accountAmount;
    private final int accountThreshold;
    private final String accountEncryptionKey;
    private final int accountIndex;
    private final Baker accountBaker;
    private final EncryptedAmount accountEncryptedAmount;
    private final ReleaseSchedule accountReleaseSchedule;
    private final Map<String, Credential> accountCredentials;

    @JsonCreator
    AccountInfo(@JsonProperty("accountNonce") int accountNonce,
                @JsonProperty("accountAmount") String accountAmount,
                @JsonProperty("accountThreshold") int accountThreshold,
                @JsonProperty("accountEncryptionKey") String accountEncryptionKey,
                @JsonProperty("accountIndex") int accountIndex,
                @JsonProperty("accountBaker") Baker accountBaker,
                @JsonProperty("accountEncryptedAmount") EncryptedAmount accountEncryptedAmount,
                @JsonProperty("accountReleaseSchedule") ReleaseSchedule accountReleaseSchedule,
                @JsonProperty("accountCredentials") Map<String, Credential> accountCredentials) {
        this.accountNonce = accountNonce;
        this.accountAmount = accountAmount;
        this.accountThreshold = accountThreshold;
        this.accountEncryptionKey = accountEncryptionKey;
        this.accountIndex = accountIndex;
        this.accountBaker = accountBaker;
        this.accountEncryptedAmount = accountEncryptedAmount;
        this.accountReleaseSchedule = accountReleaseSchedule;
        this.accountCredentials = accountCredentials;
    }

    public static AccountInfo fromJson(String accountInfoJsonString) {
        try {
            return JsonMapper.INSTANCE.readValue(accountInfoJsonString, AccountInfo.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse AccountInfo JSON", e);
        }
    }
}

