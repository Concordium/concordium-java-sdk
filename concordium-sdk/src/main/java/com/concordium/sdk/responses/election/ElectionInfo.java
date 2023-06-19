package com.concordium.sdk.responses.election;

import com.concordium.sdk.responses.transactionstatus.PartsPerHundredThousand;
import com.google.common.collect.ImmutableList;
import lombok.*;

/**
 * Contains information related to baker election for a particular block.
 * Response type for getElectionInfo.
 */
@Builder
@ToString
@Getter
@EqualsAndHashCode
public class ElectionInfo {
    /**
     * Baking lottery election difficulty.
     * Note, Present only in protocol versions 1-5.
     * TODO optional getter?
     */
    private final PartsPerHundredThousand electionDifficulty;

    /**
     * Current leadership election nonce for the lottery.
     */
    private final byte[] leadershipElectionNonce;

    /**
     * List of the currently eligible bakers.
     */
    private final ImmutableList<ElectionInfoBaker> bakerElectionInfo;

    /**
     * Parses {@link com.concordium.grpc.v2.ElectionInfo} to {@link ElectionInfo}.
     *
     * @param info {@link com.concordium.grpc.v2.ElectionInfo} returned by the GRPC V2 API.
     * @return parsed {@link ElectionInfo}.
     */
    public static ElectionInfo parse(com.concordium.grpc.v2.ElectionInfo info) {
        val builder = ElectionInfo.builder();
        val bakerElectionInfo = new ImmutableList.Builder<ElectionInfoBaker>();
        info.getBakerElectionInfoList().forEach(e -> bakerElectionInfo.add(ElectionInfoBaker.parse(e)));
        if (info.hasElectionDifficulty()) {
            builder.electionDifficulty(PartsPerHundredThousand.parse(info.getElectionDifficulty().getValue()));
        }
        return builder.leadershipElectionNonce(info.getElectionNonce().getValue().toByteArray())
                .bakerElectionInfo(bakerElectionInfo.build())
                .build();
    }
}
