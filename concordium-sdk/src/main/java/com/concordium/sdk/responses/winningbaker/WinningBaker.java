package com.concordium.sdk.responses.winningbaker;

import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.responses.Round;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Details of which baker won the lottery in a given round in consensus version 1.
 */
@Getter
@Builder
@EqualsAndHashCode
@ToString
public class WinningBaker {

    private final Round round;
    private final BakerId winner;
    private final boolean present;

    /**
     * Parses {@link com.concordium.grpc.v2.WinningBaker} to {@link WinningBaker}.
     * @param winner {@link com.concordium.grpc.v2.WinningBaker} returned by the GRPC V2 API.
     * @return parsed {@link WinningBaker}.
     */
    public static WinningBaker parse(com.concordium.grpc.v2.WinningBaker winner) {
        return WinningBaker.builder()
                .round(Round.from(winner.getRound()))
                .winner(BakerId.from(winner.getWinner()))
                .present(winner.getPresent())
                .build();
    }
}
