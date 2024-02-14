package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.responses.modulelist.ModuleRef;
import com.concordium.sdk.transactions.ReceiveName;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Reference to a non-existing contract receive method.
 */
@Getter
@ToString
@Builder
public class RejectReasonInvalidReceiveMethod extends RejectReason {
    private final ModuleRef moduleRef;
    private final ReceiveName receiveName;

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_RECEIVE_METHOD;
    }
}
