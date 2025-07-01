package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.plt.TokenEffect;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public final class TokenUpdateResult implements TransactionResultEvent {
    private final TokenEffect effect;

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.TOKEN_UPDATE_EFFECT;
    }
}
