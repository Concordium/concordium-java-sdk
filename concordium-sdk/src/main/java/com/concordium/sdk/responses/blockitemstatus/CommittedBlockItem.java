package com.concordium.sdk.responses.blockitemstatus;

import com.concordium.grpc.v2.BlockItemStatus;
import com.concordium.grpc.v2.BlockItemSummaryInBlock;
import com.concordium.sdk.responses.blockitemsummary.Summary;
import com.concordium.sdk.transactions.Hash;
import lombok.*;

import java.util.Map;

/**
 * The transaction has been executed in one or more blocks,
 * but neither of those blocks are yet part of the authoritative chain i.e., finalized.
 */
@Builder
@EqualsAndHashCode
@ToString
@Getter
public class CommittedBlockItem {

    /**
     * Events emitted by the transaction when it was
     * executed in a certain block.
     */
    @Singular
    private final Map<Hash, Summary> summaries;


    public static CommittedBlockItem from(BlockItemStatus.Committed committed) {
        val builder = CommittedBlockItem.builder();
        for (BlockItemSummaryInBlock summary : committed.getOutcomesList()) {
            builder.summary(Hash.from(summary.getBlockHash()), Summary.from(summary));
        }
        return builder.build();
    }
}
