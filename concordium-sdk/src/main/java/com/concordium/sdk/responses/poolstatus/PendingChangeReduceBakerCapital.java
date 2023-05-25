package com.concordium.sdk.responses.poolstatus;

import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.Timestamp;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.time.OffsetDateTime;
import java.util.Date;

@Data
@Jacksonized
@Builder
public class PendingChangeReduceBakerCapital extends PendingChange {

    /**
     * New baker equity capital.
     */
    private final CCDAmount bakerEquityCapital;

    /**
     * Effective time of the change.
     */
    private final OffsetDateTime effectiveTime;
}
