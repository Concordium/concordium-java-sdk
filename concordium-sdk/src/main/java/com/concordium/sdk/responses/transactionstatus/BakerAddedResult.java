package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.BakerEvent;
import com.concordium.grpc.v2.BakerKeysEvent;
import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@ToString
@Getter
@EqualsAndHashCode(callSuper = true)
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

    public static BakerAddedResult from(BakerEvent.BakerAdded bakerAdded) {
        BakerKeysEvent keysEvent = bakerAdded.getKeysEvent();
        return BakerAddedResult
                .builder()
                .bakerId(BakerId.from(bakerAdded.getKeysEvent().getBakerId()))
                .account(AccountAddress.from(bakerAdded.getKeysEvent().getAccount()))
                .electionKey(bakerAdded.getKeysEvent().getElectionKey().toByteArray())
                .aggregationKey(bakerAdded.getKeysEvent().getAggregationKey().toByteArray())
                .signKey(ED25519PublicKey.from(bakerAdded.getKeysEvent().getSignKey()))
                .restakeEarnings(bakerAdded.getRestakeEarnings())
                .stake(CCDAmount.from(bakerAdded.getStake()))
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_ADDED;
    }
}
