package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.BakerId;
import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.responses.transactionevent.accounttransactionresults.AccountTransactionResult;
import com.concordium.sdk.responses.transactionevent.accounttransactionresults.BakerEvent;
import com.concordium.sdk.responses.transactionevent.accounttransactionresults.BakerEventType;
import com.concordium.sdk.responses.transactionevent.accounttransactionresults.TransactionType;
import com.concordium.sdk.types.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * A baker was removed.
 */
@ToString(callSuper = true)
@Getter
@EqualsAndHashCode(callSuper = true)
public final class BakerRemovedResult extends AbstractBakerResult implements AccountTransactionResult, BakerEvent {

    @Builder
    @JsonCreator
    BakerRemovedResult(@JsonProperty("bakerId") AccountIndex bakerId,
                       @JsonProperty("account") AccountAddress account) {
        super(bakerId, account);
    }

    /**
     * Parses {@link BakerId} and {@link com.concordium.grpc.v2.AccountAddress} to {@link BakerRemovedResult}.
     * @param bakerRemoved {@link BakerId} returned by the GRPC V2 API.
     * @param sender {@link com.concordium.grpc.v2.AccountAddress} returned by the GRPC V2 API.
     * @return parsed {@link BakerRemovedResult}
     */
    public static BakerRemovedResult parse(BakerId bakerRemoved, com.concordium.grpc.v2.AccountAddress sender) {
        return BakerRemovedResult.builder()
                .bakerId(AccountIndex.from(bakerRemoved.getValue()))
                .account(AccountAddress.parse(sender))
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_REMOVED;
    }

    @Override
    public TransactionType getResultType() {
        return TransactionType.REMOVE_BAKER;
    }

    @Override
    public BakerEventType getBakerEventType() {
        return BakerEventType.BAKER_REMOVED;
    }
}
