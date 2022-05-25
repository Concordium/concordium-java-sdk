package com.concordium.sdk.responses.transactionstatus;

import lombok.ToString;

/**
 * Transaction fee commission is not in the valid range for a baker
 */
@ToString
public class RejectReasonTransactionFeeCommissionNotInRange extends RejectReason{
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.TRANSACTION_FEE_COMMISSION_NOT_IN_RANGE;
    }
}
