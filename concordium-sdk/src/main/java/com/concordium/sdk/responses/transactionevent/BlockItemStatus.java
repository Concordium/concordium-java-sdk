package com.concordium.sdk.responses.transactionevent;

import com.concordium.sdk.responses.transactionstatus.Status;
import com.google.common.collect.ImmutableList;
import lombok.*;

import java.util.List;

/**
 * Status of a block item known to the node.
 */
@Builder
@EqualsAndHashCode
@ToString
@Getter
public class BlockItemStatus {

    /**
     * Status of the block item.
     */
    private Status status;

    /**
     * {@link BlockItemSummaryInBlock}s of the block item.
     * Contains no elements if status is {@link Status#RECEIVED}.
     * Contains exactly one element if status is {@link Status#FINALIZED}.
     * Contains one or more elements if status is {@link Status#COMMITTED}.
     */
    private List<BlockItemSummaryInBlock> outcomes;

    /**
     * Parses {@link com.concordium.grpc.v2.BlockItemStatus} to {@link BlockItemStatus}.
     * @param status {@link com.concordium.grpc.v2.BlockItemStatus} returned by the GRPC V2 API.
     * @return parsed {@link BlockItemStatus}.
     */
    public static BlockItemStatus parse (com.concordium.grpc.v2.BlockItemStatus status) {
        val outcomes = new ImmutableList.Builder<BlockItemSummaryInBlock>();
        val builder = BlockItemStatus.builder();
        switch (status.getStatusCase()) {
            case RECEIVED:
                builder.status(Status.RECEIVED);
                break;
            case COMMITTED:
                builder.status(Status.COMMITTED);
                status.getCommitted().getOutcomesList().forEach(
                        e -> outcomes.add(BlockItemSummaryInBlock.parse(e))
                );
                break;
            case FINALIZED:
                builder.status(Status.FINALIZED);
                outcomes.add(BlockItemSummaryInBlock.parse(status.getFinalized().getOutcome()));
                break;
            case STATUS_NOT_SET:
                throw new IllegalArgumentException();
        }
        return builder
                .outcomes(outcomes.build())
                .build();
    }
}
