package com.concordium.sdk.responses.accountinfo;

import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.transactions.Hash;
import com.concordium.sdk.types.Timestamp;
import com.google.common.collect.ImmutableList;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

import static com.google.common.collect.ImmutableList.copyOf;

/**
 * A CCD release in the future.
 */
@Getter
@ToString
@EqualsAndHashCode
@Builder
@Jacksonized
public final class ScheduledRelease {

    /**
     * The time when the CCD will be released for the account.
     */
    private final Timestamp timestamp;

    /**
     * The amount that will be released.
     */
    private final CCDAmount amount;

    /**
     * A list of 'origin' transactions for the {@link ScheduledRelease}
     */
    @Singular
    private final List<Hash> transactions;

    /**
     * A list of 'origin' transactions for the {@link ScheduledRelease}
     */
    public ImmutableList<Hash> getTransactions() {
        return copyOf(transactions);
    }
}
