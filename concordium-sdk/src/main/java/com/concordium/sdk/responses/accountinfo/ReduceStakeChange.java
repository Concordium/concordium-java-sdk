package com.concordium.sdk.responses.accountinfo;

import com.concordium.sdk.transactions.CCDAmount;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * The baker has reduced its stake.
 */
@ToString(callSuper = true)
@Getter
@Jacksonized
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class ReduceStakeChange extends PendingChange {

    /**
     * The new stake for the baker.
     */
    private final CCDAmount newStake;
}
