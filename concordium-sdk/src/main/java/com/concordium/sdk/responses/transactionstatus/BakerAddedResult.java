package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.BakerEvent;
import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@ToString(callSuper = true)
@Getter
@SuperBuilder
public final class BakerAddedResult extends AbstractBakerChangeResult {
    private final boolean restakeEarnings;
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


    /**
     * Parses {@link com.concordium.grpc.v2.BakerEvent.BakerAdded} to {@link BakerAddedResult}.
     * @param bakerAdded {@link com.concordium.grpc.v2.BakerEvent.BakerAdded} returned by the GRPC V2 API.
     * @return parsed {@link BakerAddedResult}.
     */
    public static BakerAddedResult parse(BakerEvent.BakerAdded bakerAdded) {
        return BakerAddedResult.builder()
                .bakerId(AccountIndex.from(bakerAdded.getKeysEvent().getBakerId().getValue()))
                .account(AccountAddress.parse(bakerAdded.getKeysEvent().getAccount()))
                .signKey(bakerAdded.getKeysEvent().getSignKey().getValue().toByteArray())
                .electionKey(bakerAdded.getKeysEvent().getElectionKey().getValue().toByteArray())
                .aggregationKey(bakerAdded.getKeysEvent().getAggregationKey().getValue().toByteArray())
                .stake(CCDAmount.fromMicro(bakerAdded.getStake().getValue()))
                .restakeEarnings(bakerAdded.getRestakeEarnings())
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_ADDED;
    }
}
