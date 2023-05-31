package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.DelegatorId;
import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.responses.transactionevent.accounttransactionresults.DelegationEvent;
import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * The sender of the transaction has started delegating.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class DelegationAdded extends AbstractDelegatorResult implements DelegationEvent {


    @JsonCreator
    DelegationAdded(@JsonProperty("delegatorId") AccountIndex delegatorId,
                    @JsonProperty("account") AccountAddress delegatorAddress) {
        super(delegatorId, delegatorAddress);
    }

    /**
     * Parses {@link DelegatorId} and {@link com.concordium.grpc.v2.AccountAddress} to {@link DelegationAdded}.
     * @param delegationAdded {@link DelegatorId} returned by the GRPC V2 API.
     * @param sender {@link com.concordium.grpc.v2.AccountAddress} returned by the GRPC V2 API.
     * @return parsed {@link DelegationAdded}.
     */
    public static DelegationAdded parse(DelegatorId delegationAdded, com.concordium.grpc.v2.AccountAddress sender) {
        return DelegationAdded.builder()
                .delegatorId(AccountIndex.from(delegationAdded.getId().getValue()))
                .delegatorAddress(AccountAddress.parse(sender))
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.DELEGATION_ADDED;
    }
}
