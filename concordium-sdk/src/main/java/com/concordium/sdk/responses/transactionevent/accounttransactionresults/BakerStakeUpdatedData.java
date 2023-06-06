package com.concordium.sdk.responses.transactionevent.accounttransactionresults;

import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.transactions.CCDAmount;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class BakerStakeUpdatedData {

    /**
     * The affected baker.
     */
    private BakerId bakerId;

    /**
     * The new stake.
     */
    private CCDAmount newStake;

    /**
     * Whether the stake was increased or decreased.
     */
    private boolean increased;

    /**
     * Parses {@link com.concordium.grpc.v2.BakerStakeUpdatedData} to {@link BakerStakeUpdatedData}.
     * @param data {@link com.concordium.grpc.v2.BakerStakeUpdatedData} returned by the GRPC V2 API.
     * @return parsed {@link BakerStakeUpdatedData}.
     */
    public static BakerStakeUpdatedData parse(com.concordium.grpc.v2.BakerStakeUpdatedData data) {
        return BakerStakeUpdatedData.builder()
                .bakerId(BakerId.from(data.getBakerId().getValue()))
                .newStake(CCDAmount.fromMicro(data.getNewStake().getValue()))
                .increased(data.getIncreased())
                .build();
    }
}
