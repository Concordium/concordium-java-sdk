package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.BakerEvent;
import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.types.AccountAddress;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Removed an existing delegator.
 */
@Getter
@ToString
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class BakerDelegationRemoved extends AbstractBakerResult {

    private final AccountIndex delegatorId;

    public static BakerDelegationRemoved from(BakerEvent.DelegationRemoved bakerDelegationRemoved, AccountAddress sender) {
        return BakerDelegationRemoved
                .builder()
                .delegatorId(AccountIndex.from(bakerDelegationRemoved.getDelegatorId()))
                .account(sender)
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_DELEGATION_REMOVED;
    }
}
