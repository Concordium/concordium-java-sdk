package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.BakerStakeUpdatedData;
import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.responses.transactionevent.accounttransactionresults.BakerEvent;
import com.concordium.sdk.responses.transactionevent.accounttransactionresults.BakerEventType;
import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Baker stake decreased.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public final class BakerStakeDecreasedResult extends AbstractBakerResult implements BakerEvent {

    /**
     * The new stake.
     */
    private final CCDAmount newStake;

    @JsonCreator
    BakerStakeDecreasedResult(@JsonProperty("bakerId") AccountIndex bakerId,
                              @JsonProperty("account") AccountAddress account,
                              @JsonProperty("newStake") String newStake) {
        super(bakerId, account);
        this.newStake = CCDAmount.fromMicro(newStake);
    }

    /**
     * Parses {@link BakerStakeUpdatedData} and {@link com.concordium.grpc.v2.AccountAddress} to {@link BakerStakeDecreasedResult}.
     * @param update {@link BakerStakeUpdatedData} returned by the GRPC V2 API.
     * @param sender {@link com.concordium.grpc.v2.AccountAddress} returned by the GRPC V2 API.
     * @return parsed {@link BakerStakeDecreasedResult}
     */
    public static BakerStakeDecreasedResult parse(BakerStakeUpdatedData update, com.concordium.grpc.v2.AccountAddress sender) {
        return BakerStakeDecreasedResult.builder()
                .bakerId(AccountIndex.from(update.getBakerId().getValue()))
                .account(AccountAddress.parse(sender))
                .newStake(CCDAmount.fromMicro(update.getNewStake().getValue()))
                .build();
    }

    /**
     * Parses {@link com.concordium.grpc.v2.BakerEvent.BakerStakeDecreased} and {@link com.concordium.grpc.v2.AccountAddress} to {@link BakerStakeDecreasedResult}.
     * @param bakerStakeDecreased {@link com.concordium.grpc.v2.BakerEvent.BakerStakeDecreased} returned by the GRPC V2 API.
     * @param sender {@link com.concordium.grpc.v2.AccountAddress} returned by the GRPC V2 API.
     * @return parsed {@link BakerStakeDecreasedResult}
     */
    public static BakerStakeDecreasedResult parse(com.concordium.grpc.v2.BakerEvent.BakerStakeDecreased bakerStakeDecreased, com.concordium.grpc.v2.AccountAddress sender) {
        return BakerStakeDecreasedResult.builder()
                .bakerId(AccountIndex.from(bakerStakeDecreased.getBakerId().getValue()))
                .account(AccountAddress.parse(sender))
                .newStake(CCDAmount.fromMicro(bakerStakeDecreased.getNewStake().getValue()))
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_STAKE_DECREASED;
    }

    @Override
    public BakerEventType getBakerEventType() {
        return BakerEventType.BAKER_STAKE_DECREASED;
    }
}
