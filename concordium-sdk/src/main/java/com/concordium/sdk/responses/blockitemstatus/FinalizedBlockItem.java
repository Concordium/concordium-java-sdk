package com.concordium.sdk.responses.blockitemstatus;

import com.concordium.grpc.v2.BlockItemStatus;
import com.concordium.sdk.responses.blockitemsummary.Summary;
import com.concordium.sdk.transactions.Hash;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;


/**
 * The transaction has been executed in a block that
 * is part of the authoritative chain.
 */
@EqualsAndHashCode
@Getter
@Builder
@ToString
public class FinalizedBlockItem {

    /**
     * Events emitted by the transaction when it was
     * executed in a block.
     */
    private final Summary summary;

    /**
     * The block that the transaction is part of.
     */
    private final Hash blockHash;


    public static FinalizedBlockItem from(BlockItemStatus.Finalized finalized) {
        return FinalizedBlockItem
                .builder()
                .summary(Summary.from(finalized.getOutcome()))
                .blockHash(Hash.from(finalized.getOutcome().getBlockHash()))
                .build();
    }
}
