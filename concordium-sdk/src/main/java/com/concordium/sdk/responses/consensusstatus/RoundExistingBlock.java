package com.concordium.sdk.responses.consensusstatus;

import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.responses.Round;
import com.concordium.sdk.transactions.Hash;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@EqualsAndHashCode
public class RoundExistingBlock {

    /**
     * The round for which the node saw a block.
     */
    private final Round round;

    /**
     * The baker that baked the block.
     */
    private final BakerId baker;

    /**
     * The hash of the block
     */
    private final Hash block;
}
