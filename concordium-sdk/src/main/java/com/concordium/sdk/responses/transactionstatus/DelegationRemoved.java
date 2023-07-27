package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.DelegatorId;
import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.types.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Delegator stopped its delegation.
 */
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class DelegationRemoved extends AbstractDelegatorResult {

    @JsonCreator
    DelegationRemoved(@JsonProperty("delegatorId") AccountIndex delegatorId,
                      @JsonProperty("account") AccountAddress delegatorAddress) {
        super(delegatorId, delegatorAddress);
    }

    public static DelegationRemoved from(DelegatorId delegationRemoved, AccountAddress sender) {
        return DelegationRemoved
                .builder()
                .delegatorId(AccountIndex.from(delegationRemoved))
                .delegatorAddress(sender)
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.DELEGATION_REMOVED;
    }
}
