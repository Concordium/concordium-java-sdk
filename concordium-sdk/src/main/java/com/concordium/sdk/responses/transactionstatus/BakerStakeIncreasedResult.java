package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.BakerEvent;
import com.concordium.grpc.v2.BakerStakeUpdatedData;
import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@SuperBuilder
public final class BakerStakeIncreasedResult extends AbstractBakerResult {
    private final CCDAmount newStake;

    @JsonCreator
    BakerStakeIncreasedResult(@JsonProperty("bakerId") AccountIndex bakerId,
                              @JsonProperty("account") AccountAddress account,
                              @JsonProperty("newStake") String newStake) {
        super(bakerId, account);
        this.newStake = CCDAmount.fromMicro(newStake);
    }

    /**
     * Parses {@link BakerStakeUpdatedData} and {@link com.concordium.grpc.v2.AccountAddress} to {@link BakerStakeIncreasedResult}.
     * @param update {@link BakerStakeUpdatedData} returned by the GRPC V2 API.
     * @param sender {@link com.concordium.grpc.v2.AccountAddress} returned by the GRPC V2 API.
     * @return parsed {@link BakerStakeIncreasedResult}
     */
    public static BakerStakeIncreasedResult parse(BakerStakeUpdatedData update, com.concordium.grpc.v2.AccountAddress sender) {
        return BakerStakeIncreasedResult.builder()
                .bakerId(AccountIndex.from(update.getBakerId().getValue()))
                .account(AccountAddress.parse(sender))
                .newStake(CCDAmount.fromMicro(update.getNewStake().getValue()))
                .build();

    }

    /**
     * Parses {@link BakerEvent.BakerStakeIncreased} and {@link com.concordium.grpc.v2.AccountAddress} to {@link BakerStakeIncreasedResult}.
     * @param bakerStakeIncreased {@link BakerEvent.BakerStakeIncreased} returned by the GRPC V2 API.
     * @param sender {@link com.concordium.grpc.v2.AccountAddress} returned by the GRPC V2 API.
     * @return parsed {@link BakerStakeIncreasedResult}
     */
    public static BakerStakeIncreasedResult parse(BakerEvent.BakerStakeIncreased bakerStakeIncreased, com.concordium.grpc.v2.AccountAddress sender) {
        return BakerStakeIncreasedResult.builder()
                .bakerId(AccountIndex.from(bakerStakeIncreased.getBakerId().getValue()))
                .account(AccountAddress.parse(sender))
                .newStake(CCDAmount.fromMicro(bakerStakeIncreased.getNewStake().getValue()))
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_STAKE_INCREASED;
    }
}
