package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.BakerEvent;
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

@Getter
@ToString
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public final class BakerStakeDecreasedResult extends BakerStakeUpdated {
    private final CCDAmount newStake;

    @JsonCreator
    BakerStakeDecreasedResult(@JsonProperty("bakerId") AccountIndex bakerId,
                              @JsonProperty("account") AccountAddress account,
                              @JsonProperty("newStake") long newStake) {
        super(bakerId, account);
        this.newStake = CCDAmount.fromMicro(newStake);
    }

    public static BakerStakeDecreasedResult from(BakerEvent.BakerStakeDecreased bakerStakeDecreased, AccountAddress sender) {
        return BakerStakeDecreasedResult
                .builder()
                .newStake(CCDAmount.from(bakerStakeDecreased.getNewStake()))
                .bakerId(BakerId.from(bakerStakeDecreased.getBakerId()))
                .account(sender)
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_STAKE_DECREASED;
    }
}
