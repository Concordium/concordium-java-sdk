package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.responses.modulelist.ModuleRef;
import com.concordium.sdk.transactions.InitName;
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
    private final ModuleRef moduleRef;
    private final InitName initName;

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_INIT_METHOD;
    }
}
