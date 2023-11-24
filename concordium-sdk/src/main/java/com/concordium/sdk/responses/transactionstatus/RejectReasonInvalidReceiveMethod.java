package com.concordium.sdk.responses.transactionstatus;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Reference to a non-existing contract receive method.
 */
@Getter
@ToString
@Builder
class RejectReasonInvalidReceiveMethod extends RejectReason {
    private final String moduleRef;
    private final String receiveName;

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_RECEIVE_METHOD;
    }
}
