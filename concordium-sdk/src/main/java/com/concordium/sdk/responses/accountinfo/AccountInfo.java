package com.concordium.sdk.responses.accountinfo;

import com.concordium.sdk.crypto.elgamal.ElgamalPublicKey;
import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.responses.accountinfo.credential.Credential;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.transactions.Index;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.Nonce;
import com.google.common.collect.ImmutableMap;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Singular;
import lombok.extern.jackson.Jacksonized;

import java.util.Objects;

/**
 * Information of an account on chain.
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@Jacksonized
@Builder
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
     * <p>
     * Note. the account threshold is a positive number <i>t</i> <= n, where n is
     * the number of credentials associated with the account.
     * See {@link AccountInfo#accountCredentials}
     */
    private final int accountThreshold;
    /**
     * The encryption key for the account.
     * Note. This is the encryption key used for receiving encrypted amounts.
     */
    private final ElgamalPublicKey accountEncryptionKey;
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
     * If the account has an {@link AccountEncryptedAmount} associated.
     */
    private final AccountEncryptedAmount accountEncryptedAmount;
    /**
     * If there is a release schedule associated with the account.
     * See {@link ReleaseSchedule}
     */
    private final ReleaseSchedule accountReleaseSchedule;
    /**
     * The credentials associated with this account.
     */
    @Singular
    private final ImmutableMap<Index, Credential> accountCredentials;

    /**
     * If the account is delegating then this is non-null.
     */
    private final AccountDelegation delegation;

    public boolean isBaker() {
        return !Objects.isNull(bakerInfo);
    }
}

