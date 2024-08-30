package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.DelegatorId;
import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.types.AccountAddress;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * The sender of the transaction has started delegating.
 */
@Getter
@ToString
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class DelegationAdded extends AbstractDelegatorResult {

    private final AccountIndex delegatorId;

    public static DelegationAdded from(DelegatorId delegationAdded, AccountAddress sender) {
        return DelegationAdded
                .builder()
                .delegatorId(AccountIndex.from(delegationAdded))
                .delegatorAddress(sender)
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.DELEGATION_ADDED;
    }
}
