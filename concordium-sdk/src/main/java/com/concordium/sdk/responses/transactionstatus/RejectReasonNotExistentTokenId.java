package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.plt.TokenId;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * The provided identifier does not match a token currently on chain.
 * Introduced in protocol version 9.
 */
@ToString
@Getter
@Builder
public class RejectReasonNotExistentTokenId extends RejectReason {
    private final TokenId tokenId;

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.NOT_EXISTENT_TOKEN_ID;
    }
}
