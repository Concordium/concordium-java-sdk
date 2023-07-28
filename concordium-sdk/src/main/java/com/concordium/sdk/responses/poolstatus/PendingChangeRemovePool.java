package com.concordium.sdk.responses.poolstatus;

import com.concordium.sdk.types.Timestamp;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.jackson.Jacksonized;


@Data
@Jacksonized
@Builder
@EqualsAndHashCode(callSuper = true)
public class PendingChangeRemovePool extends PendingChange {

    /**
     * Effective time of the change.
     */
    private final Timestamp effectiveTime;
}
