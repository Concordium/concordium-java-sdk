package com.concordium.sdk.responses.poolstatus;

import com.concordium.sdk.types.Timestamp;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;


@Data
@Jacksonized
@Builder
public class PendingChangeRemovePool extends PendingChange {

    /**
     * Effective time of the change.
     */
    private final Timestamp effectiveTime;
}
