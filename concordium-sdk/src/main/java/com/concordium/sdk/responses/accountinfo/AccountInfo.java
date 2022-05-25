package com.concordium.sdk.responses.accountinfo;

import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.responses.accountinfo.credential.Credential;
import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.transactions.Index;
import com.concordium.sdk.types.Nonce;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;
import java.util.Objects;

/**
 * AccountInfo
 */
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public final class AccountInfo {
    /**
     * The account address.
     */
    @EqualsAndHashCode.Include
    private final AccountAddress accountAddress;
    /**
     * The current nonce for the account.
     * That is, this is the first unused nonce from the perspective of the block that
     * `AccountInfo` is for.
     */
    private final Nonce accountNonce;
    /**
     * The amount of CCD owned by this account.
     */
    private final CCDAmount accountAmount;
    /**
     * The account threshold for this account i.e., how
     * many credentials that needs to sign transactions for this account.
     *
     * Note. the account threshold is a positive number <i>t</i> <= n, where n is
     * the number of credentials associated with the account.
     * See {@link AccountInfo#accountCredentials}
     */
    private final int accountThreshold;
    /**
     * The encryption key for the account.
     * Note. This is the encryption key used for receiving encrypted amounts.
     */
    private final String accountEncryptionKey;
    /**
     * A positive and sequential index of the account which is increasing in the order of creation.
     * If the account is registered as a baker, then this is also
     * the `baker id`.
     */
    private final AccountIndex accountIndex;
    /**
     * If the account is registered as a baker, then this will be not null
     * containing {@link Baker} information.
     */
    private final Baker bakerInfo;
    /**
     * If the account has an {@link EncryptedAmount} associated.
     */
    private final EncryptedAmount accountEncryptedAmount;
    /**
     * If there is a release schedule associated with the account.
     * See {@link ReleaseSchedule}
     */
    private final ReleaseSchedule accountReleaseSchedule;
    /**
     * The credentials associated with this account.
     */
    private final Map<Index, Credential> accountCredentials;

    /**
     * If the account is delegating then this is non null.
     */
    private final AccountDelegation delegation;

    @JsonCreator
    AccountInfo(@JsonProperty("accountAddress") AccountAddress accountAddress,
                @JsonProperty("accountNonce") Nonce accountNonce,
                @JsonProperty("accountAmount") CCDAmount accountAmount,
                @JsonProperty("accountThreshold") int accountThreshold,
                @JsonProperty("accountEncryptionKey") String accountEncryptionKey,
                @JsonProperty("accountIndex") AccountIndex accountIndex,
                @JsonProperty("accountBaker") Baker bakerInfo,
                @JsonProperty("accountEncryptedAmount") EncryptedAmount accountEncryptedAmount,
                @JsonProperty("accountReleaseSchedule") ReleaseSchedule accountReleaseSchedule,
                @JsonProperty("accountCredentials") Map<Index, Credential> accountCredentials,
                @JsonProperty("accountDelegation") AccountDelegation delegation) {
        this.accountAddress = accountAddress;
        this.accountNonce = accountNonce;
        this.accountAmount = accountAmount;
        this.accountThreshold = accountThreshold;
        this.accountEncryptionKey = accountEncryptionKey;
        this.accountIndex = accountIndex;
        this.bakerInfo = bakerInfo;
        this.accountEncryptedAmount = accountEncryptedAmount;
        this.accountReleaseSchedule = accountReleaseSchedule;
        this.accountCredentials = accountCredentials;
        this.delegation = delegation;
    }

    public static AccountInfo fromJson(String accountInfoJsonString) {
        try {
            return JsonMapper.INSTANCE.readValue(accountInfoJsonString, AccountInfo.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse AccountInfo JSON", e);
        }
    }

    public boolean isBaker() {
        return !Objects.isNull(bakerInfo);
    }
}

