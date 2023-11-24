package com.concordium.sdk.responses.transactionstatus;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Reference to a non-existing contract init method.
 */
@Getter
@ToString
@Builder
public class RejectReasonInvalidInitMethod extends RejectReason {
    private final String moduleRef;
    private final String initName;

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_INIT_METHOD;
    }
}
