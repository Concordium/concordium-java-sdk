package com.concordium.sdk.responses;

import com.concordium.sdk.responses.accountinfo.PendingChange;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.AccountAddress;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Optional;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class DelegatorInfo {
    /**
     * The delegator account address.
     */
    private final AccountAddress account;

    /**
     * The amount of stake currently staked to the pool.
     */
    private final CCDAmount stake;

    /**
     * Pending change to the current stake of the delegator.
     */
    private final Optional<PendingChange> pendingChange;
}
