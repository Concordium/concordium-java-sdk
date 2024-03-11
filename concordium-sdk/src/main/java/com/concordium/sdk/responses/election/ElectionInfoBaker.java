package com.concordium.sdk.responses.election;

import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.types.AccountAddress;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Baker Information in {@link ElectionInfo}.
 */
@Builder
@ToString
@EqualsAndHashCode
@Getter
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
}
