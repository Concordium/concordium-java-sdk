package com.concordium.sdk.responses.poolstatus;

import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.Timestamp;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.jackson.Jacksonized;


@Data
@Jacksonized
@Builder
@EqualsAndHashCode(callSuper = true)
public class PendingChangeReduceBakerCapital extends PendingChange {

    /**
     * New baker equity capital.
     */
    private final CCDAmount bakerEquityCapital;

    /**
     * Effective time of the change.
     */
    private final Timestamp effectiveTime;
}
