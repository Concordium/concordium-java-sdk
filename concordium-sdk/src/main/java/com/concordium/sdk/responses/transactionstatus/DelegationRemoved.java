package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.DelegatorId;
import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Delegator stopped its delegation.
 */
@Getter
@Setter
@SuperBuilder
public class DelegationRemoved extends AbstractDelegatorResult {

    @JsonCreator
    DelegationRemoved(@JsonProperty("delegatorId") AccountIndex delegatorId,
                      @JsonProperty("account") AccountAddress delegatorAddress) {
        super(delegatorId, delegatorAddress);
    }

    /**
     * Parses {@link DelegatorId} and {@link com.concordium.grpc.v2.AccountAddress} to {@link DelegationRemoved}.
     * @param delegationRemoved {@link DelegatorId} returned by the GRPC V2 API.
     * @param sender {@link com.concordium.grpc.v2.AccountAddress} returned by the GRPC V2 API.
     * @return parsed {@link DelegationRemoved}.
     */
    public static DelegationRemoved parse(DelegatorId delegationRemoved, com.concordium.grpc.v2.AccountAddress sender) {
        return DelegationRemoved.builder()
                .delegatorId(AccountIndex.from(delegationRemoved.getId().getValue()))
                .delegatorAddress(AccountAddress.parse(sender))
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.DELEGATION_REMOVED;
    }
}
