package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.BakerKeysEvent;
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
 * A baker's keys were updated.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class BakerKeysUpdatedResult extends AbstractBakerChangeResult implements AccountTransactionResult, BakerEvent {


    @JsonCreator
    BakerKeysUpdatedResult(@JsonProperty("bakerId") AccountIndex bakerId,
                           @JsonProperty("account") AccountAddress account,
                           @JsonProperty("electionKey") String electionKey,
                           @JsonProperty("aggregationKey") String aggregationKey,
                           @JsonProperty("signKey") String signKey) {
        super(bakerId, account, electionKey, aggregationKey, signKey);
    }

    @Builder
    BakerKeysUpdatedResult(
            AccountIndex bakerId,
            AccountAddress account,
            byte[] electionKey,
            byte[] aggregationKey,
            byte[] signKey) {
        super(bakerId, account, electionKey, aggregationKey, signKey);
    }

    /**
     * Parses {@link BakerKeysEvent} to {@link BakerKeysUpdatedResult}.
     * @param bakerKeysUpdated {@link BakerKeysEvent} returned by the GRPC V2 API.
     * @return parsed {@link BakerKeysEvent}.
     */
    public static BakerKeysUpdatedResult parse(BakerKeysEvent bakerKeysUpdated) {
        return BakerKeysUpdatedResult.builder()
                .bakerId(AccountIndex.from(bakerKeysUpdated.getBakerId().getValue()))
                .account(AccountAddress.parse(bakerKeysUpdated.getAccount()))
                .electionKey(bakerKeysUpdated.getElectionKey().getValue().toByteArray())
                .aggregationKey(bakerKeysUpdated.getAggregationKey().getValue().toByteArray())
                .signKey(bakerKeysUpdated.getSignKey().getValue().toByteArray())
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_KEYS_UPDATED;
    }

    @Override
    public TransactionType getResultType() {
        return TransactionType.UPDATE_BAKER_KEYS;
    }

    @Override
    public BakerEventType getBakerEventType() {
        return BakerEventType.BAKER_KEYS_UPDATED;
    }
}
