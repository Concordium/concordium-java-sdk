package com.concordium.sdk.responses;

import com.concordium.sdk.responses.blocksummary.updates.queues.PendingUpdateType;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * A pending update to the Chain Parameters.
 */
@Builder
@ToString
@EqualsAndHashCode
@Getter
public class PendingUpdateV2<T> {
    /**
     * The effective time of the update.
     */
    private final UInt64 effectiveTime;

    /**
     * Type ({@link PendingUpdateType}) of the Update
     */
    private final PendingUpdateType type;

    /**
     * The effect of the update.
     */
    private final T update;
}
