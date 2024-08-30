package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.DelegationEvent;
import com.concordium.sdk.types.AccountAddress;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class DelegationBakerRemoved extends AbstractDelegatorResult {

    private final long bakerId;

    public static DelegationBakerRemoved from(DelegationEvent.BakerRemoved delegationBakerRemoved, AccountAddress sender) {
        return DelegationBakerRemoved
                .builder()
                .bakerId(delegationBakerRemoved.getBakerId().getValue())
                .delegatorAddress(sender)
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.DELEGATION_BAKER_REMOVED;
    }
}
