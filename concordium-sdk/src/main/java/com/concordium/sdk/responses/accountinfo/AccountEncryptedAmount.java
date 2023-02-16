package com.concordium.sdk.responses.accountinfo;

import com.concordium.sdk.transactions.EncryptedAmount;
import com.concordium.sdk.transactions.EncryptedAmountIndex;
import com.google.common.collect.ImmutableList;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.Optional;

import static com.google.common.collect.ImmutableList.copyOf;

/**
 * The state of the encrypted balance of an account.
 */
@Data
@Jacksonized
@Builder
public final class AccountEncryptedAmount {

    @Singular
    private final List<EncryptedAmount> incomingAmounts;

    /**
     * Amounts starting at `start_index` (or at `start_index + 1` if there is
     * an aggregated amount present). They are assumed to be numbered sequentially.
     * The length of this list is bounded by the maximum number
     * of incoming amounts on the accounts, which is currently 32. After that aggregation kicks in.
     */
    public ImmutableList<EncryptedAmount> getIncomingAmounts() {
        return copyOf(incomingAmounts);
    }

    /**
     * Encrypted amount that is a result of this account's actions.
     * In particular this list includes the aggregate of
     * <br/> - remaining amounts that result when transferring to public balance
     * <br/> - remaining amounts when transferring to another account
     * <br/> - encrypted amounts that are transferred from public balance
     * <br/> When a transfer is made all of these must always be used.
     */
    private final EncryptedAmount selfAmount;

    /**
     * Starting index for incoming encrypted amounts. If an aggregated amount
     * is present then this index is associated with such an amount and the
     * list of incoming encrypted amounts starts at the index `start_index + 1`.
     */
    private final EncryptedAmountIndex startIndex;

    /**
     * If present, the amount that has resulted from aggregating other amounts
     * If this field is present so is `num_aggregated`.
     */
    private final Optional<EncryptedAmount> aggregatedAmount;

    /**
     * The number of aggregated amounts (must be at least 2 if present). This
     * field is present if and only if `aggregated_amount` is present.
     */
    private final Optional<Integer> numAggregated;
}
