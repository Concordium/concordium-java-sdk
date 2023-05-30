package com.concordium.sdk.responses.transactionstatus;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * A configure delegation transaction is missing one or more arguments in order to add a delegator.
 */
@ToString
@EqualsAndHashCode
public class RejectReasonMissingDelegationAddParameters extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.MISSING_DELEGATION_ADD_PARAMETERS;
    }
}
