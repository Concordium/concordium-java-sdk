package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.responses.transactionevent.accounttransactionresults.DelegationEvent;
import com.concordium.sdk.responses.transactionevent.accounttransactionresults.DelegationEventType;
import com.concordium.sdk.types.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * The delegator set its restake property.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DelegationSetRestakeEarnings extends AbstractDelegatorResult implements DelegationEvent {


    /**
     * Whether earnings should be automatically restaked or not.
     */
    private final boolean restakeEarnings;

    @Builder
    @JsonCreator
    DelegationSetRestakeEarnings(@JsonProperty("delegatorId") AccountIndex delegatorId,
                                 @JsonProperty("account") AccountAddress delegatorAddress,
                                 @JsonProperty("restakeEarnings") boolean restakeEarnings) {
       super(delegatorId, delegatorAddress);
        this.restakeEarnings = restakeEarnings;
    }

    /**
     * Parses {@link com.concordium.grpc.v2.DelegationEvent.DelegationSetRestakeEarnings} and {@link com.concordium.grpc.v2.AccountAddress} to {@link DelegationSetRestakeEarnings}.
     * @param delegationSetRestakeEarnings {@link com.concordium.grpc.v2.DelegationEvent.DelegationSetRestakeEarnings} returned by the GRPC V2 API.
     * @param sender {@link com.concordium.grpc.v2.AccountAddress} returned by the GRPC V2 API.
     * @return parsed {@link DelegationSetRestakeEarnings}.
     */
    public static DelegationSetRestakeEarnings parse(com.concordium.grpc.v2.DelegationEvent.DelegationSetRestakeEarnings delegationSetRestakeEarnings, com.concordium.grpc.v2.AccountAddress sender) {
        return DelegationSetRestakeEarnings.builder()
                .delegatorId(AccountIndex.from(delegationSetRestakeEarnings.getDelegatorId().getId().getValue()))
                .delegatorAddress(AccountAddress.parse(sender))
                .restakeEarnings(delegationSetRestakeEarnings.getRestakeEarnings())
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.DELEGATION_SET_RESTAKE_EARNINGS;
    }

    @Override
    public DelegationEventType getDelegationEventType() {
        return DelegationEventType.DELEGATION_SET_RESTAKE_EARNINGS;
    }
}
