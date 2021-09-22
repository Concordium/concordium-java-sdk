package com.concordium.sdk.responses.transactionstatus;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
abstract class RejectReasonContent {
    // Used for tagging the underlying RejectReasonContent

    /**
     * Provides a convenient way of getting the type
     * @return the {@link RejectReasonType} for the given instance
     */
    public abstract RejectReasonType getType();
}
