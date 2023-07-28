package com.concordium.sdk.responses.blockitemstatus;

import com.concordium.sdk.responses.transactionstatus.Status;
import lombok.*;

import java.util.Optional;

/**
 * Status for a block item sent to a node.
 * Refer to {@link Status} for the different states it can be in.
 */
@Getter
@ToString
@EqualsAndHashCode
@Builder
public class BlockItemStatus {
    private final Status status;

    private final CommittedBlockItem committedBlockItem;

    public Optional<CommittedBlockItem> getCommittedBlockItem() {
        if (this.status == Status.COMMITTED) {
            return Optional.of(committedBlockItem);
        }
        return Optional.empty();
    }

    private final FinalizedBlockItem finalizedBlockItem;

    public Optional<FinalizedBlockItem> getFinalizedBlockItem() {
        if (this.status == Status.FINALIZED) {
            return Optional.of(finalizedBlockItem);
        }
        return Optional.empty();
    }

    public static BlockItemStatus from(com.concordium.grpc.v2.BlockItemStatus value) {
        val builder = BlockItemStatus.builder();
        switch (value.getStatusCase()) {
            case RECEIVED:
                builder
                        .status(Status.RECEIVED);
                break;
            case COMMITTED:
                builder
                        .committedBlockItem(CommittedBlockItem.from(value.getCommitted()))
                        .status(Status.COMMITTED);
                break;
            case FINALIZED:
                builder
                        .finalizedBlockItem(FinalizedBlockItem.from(value.getFinalized()))
                        .status(Status.FINALIZED);
                break;
            default:
                builder.status(Status.ABSENT);
        }
        return builder.build();
    }
}
