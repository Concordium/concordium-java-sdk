package com.concordium.sdk.responses.election;

import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.transactions.AccountAddress;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Baker Information in {@link ElectionInfo}.
 */
@Builder
@ToString
@Getter
@EqualsAndHashCode
public class ElectionInfoBaker {
    /**
     * The ID of the baker.
     */
    private final BakerId baker;

    /**
     * The account address of the baker.
     */
    private final AccountAddress account;

    /**
     * The lottery power of the baker, rounded to the nearest representable "double".
     */
    private final double lotteryPower;

    /**
     * Parses {@link com.concordium.grpc.v2.ElectionInfo.Baker} to {@link ElectionInfoBaker}.
     * @param baker {@link com.concordium.grpc.v2.ElectionInfo.Baker} returned by the GRPC V2 API.
     * @return parsed {@link ElectionInfoBaker}.
     */
    public static ElectionInfoBaker parse(com.concordium.grpc.v2.ElectionInfo.Baker baker) {
        return ElectionInfoBaker.builder()
                .baker(BakerId.from(baker.getBaker().getValue()))
                .account(AccountAddress.from(baker.getAccount().getValue().toByteArray()))
                .lotteryPower(baker.getLotteryPower())
                .build();
    }
}
