package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.types.AccountAddress;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Account is not a delegation account.
 */
@ToString
@Getter
@Builder
public class RejectReasonNotADelegator extends RejectReason {
    private final AccountAddress accountAddress;

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.NOT_A_DELEGATOR;
    }
}
