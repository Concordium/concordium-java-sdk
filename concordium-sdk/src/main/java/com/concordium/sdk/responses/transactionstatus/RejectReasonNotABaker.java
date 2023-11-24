package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.types.AccountAddress;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Account is not a baker account.
 */
@ToString
@Builder
public class RejectReasonNotABaker extends RejectReason {
    @Getter
    private final AccountAddress accountAddress;

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.NOT_A_BAKER;
    }
}
