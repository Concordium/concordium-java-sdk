package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.BakerKeysEvent;
import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.responses.transactionevent.accounttransactionresults.AccountTransactionResult;
import com.concordium.sdk.responses.transactionevent.accounttransactionresults.BakerEvent;
import com.concordium.sdk.responses.transactionevent.accounttransactionresults.BakerEventType;
import com.concordium.sdk.responses.transactionevent.accounttransactionresults.TransactionType;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * A baker was added.
 */
@ToString(callSuper = true)
@Getter
@EqualsAndHashCode(callSuper = true)
public final class BakerAddedResult extends AbstractBakerChangeResult implements AccountTransactionResult, BakerEvent {

    /**
     * Whether the baker will automatically add earnings to their stake or not.
     */
    private final boolean restakeEarnings;

    /**
     * The amount the account staked to become a baker.
     */
    private final CCDAmount stake;

    @JsonCreator
    BakerAddedResult(
            @JsonProperty("bakerId") AccountIndex bakerId,
            @JsonProperty("account") AccountAddress account,
            @JsonProperty("electionKey") String electionKey,
            @JsonProperty("aggregationKey") String aggregationKey,
            @JsonProperty("signKey") String signKey,
            @JsonProperty("restakeEarnings") boolean restakeEarnings,
            @JsonProperty("stake") CCDAmount stake) {
        super(bakerId, account, electionKey, aggregationKey, signKey);
        this.restakeEarnings = restakeEarnings;
        this.stake = stake;
    }

    @Builder
    BakerAddedResult(
            AccountIndex bakerId,
            AccountAddress account,
            byte[] electionKey,
            byte[] aggregationKey,
            byte[] signKey,
            boolean restakeEarnings,
            CCDAmount stake) {
        super(bakerId, account, electionKey, aggregationKey, signKey);
        this.restakeEarnings = restakeEarnings;
        this.stake = stake;
    }


    /**
     * Parses {@link com.concordium.grpc.v2.BakerEvent.BakerAdded} to {@link BakerAddedResult}.
     * @param bakerAdded {@link com.concordium.grpc.v2.BakerEvent.BakerAdded} returned by the GRPC V2 API.
     * @return parsed {@link BakerAddedResult}.
     */
    public static BakerAddedResult parse(com.concordium.grpc.v2.BakerEvent.BakerAdded bakerAdded) {
        BakerKeysEvent keysEvent = bakerAdded.getKeysEvent();
        return BakerAddedResult.builder()
                .bakerId(AccountIndex.from(keysEvent.getBakerId().getValue()))
                .account(AccountAddress.parse(keysEvent.getAccount()))
                .signKey(keysEvent.getSignKey().getValue().toByteArray())
                .electionKey(keysEvent.getElectionKey().getValue().toByteArray())
                .aggregationKey(keysEvent.getAggregationKey().getValue().toByteArray())
                .stake(CCDAmount.fromMicro(bakerAdded.getStake().getValue()))
                .restakeEarnings(bakerAdded.getRestakeEarnings())
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_ADDED;
    }

    @Override
    public TransactionType getResultType() {
        return TransactionType.ADD_BAKER;
    }

    @Override
    public BakerEventType getBakerEventType() {
        return BakerEventType.BAKER_ADDED;
    }
}
