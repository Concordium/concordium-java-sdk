package com.concordium.sdk.responses.transactionevent;

import com.concordium.sdk.transactions.Hash;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * A {@link BlockTransactionEvent} together with a block {@link Hash}. Used in {@link BlockItemStatus}.
 */
@Builder
@Getter
@EqualsAndHashCode
@ToString
public class BlockItemSummaryInBlock {

    /**
     * The block hash.
     */
    private Hash blockHash;

    /**
     * The block item summary.
     */
    private BlockTransactionEvent outcome;

    /**
     * Parses {@link com.concordium.grpc.v2.BlockItemSummaryInBlock} to {@link BlockItemSummaryInBlock}.
     * @param summary {@link com.concordium.grpc.v2.BlockItemSummaryInBlock} returned by the GRPC V2 API.
     * @return parsed {@link BlockItemSummaryInBlock}.
     */
    public static BlockItemSummaryInBlock parse (com.concordium.grpc.v2.BlockItemSummaryInBlock summary) {
        return BlockItemSummaryInBlock.builder()
                .blockHash(Hash.from(summary.getBlockHash().getValue().toByteArray()))
                .outcome(BlockTransactionEvent.parse(summary.getOutcome()))
                .build();
    }
}
