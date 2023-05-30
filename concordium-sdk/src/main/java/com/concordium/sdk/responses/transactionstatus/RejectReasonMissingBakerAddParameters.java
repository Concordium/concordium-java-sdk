package com.concordium.sdk.responses.transactionstatus;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * A configure baker transaction is missing one or more arguments in order to add a baker.
 */
@ToString
@EqualsAndHashCode
public class RejectReasonMissingBakerAddParameters extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.MISSING_BAKER_ADD_PARAMETERS;
    }
}
