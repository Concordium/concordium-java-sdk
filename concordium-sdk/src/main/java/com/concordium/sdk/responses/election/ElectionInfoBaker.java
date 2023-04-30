package com.concordium.sdk.responses.election;

import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.transactions.AccountAddress;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Builder
@ToString
@EqualsAndHashCode
public class ElectionInfoBaker {
    /**
     * The ID of the baker.
     */
    private final AccountIndex baker;

    /**
     * The account address of the baker.
     */
    private final AccountAddress account;

    /**
     * The lottery power of the baker, rounded to the nearest representable "double".
     */
    private final double lotteryPower;
}
