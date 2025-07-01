package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.plt.TokenId;
import com.concordium.grpc.v2.plt.TokenModuleRejectReason;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * The token update transaction failed.
 * Introduced in protocol version 9.
 */
@ToString
@Getter
@Builder
public class RejectReasonTokenUpdateTransactionFailed extends RejectReason {
    private final TokenModuleRejectReason tokenModuleRejectReason;

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.NOT_EXISTENT_TOKEN_ID;
    }
}
