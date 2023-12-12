package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.types.AccountAddress;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Account does not exist
 */
@ToString
@Builder
public class RejectReasonInvalidAccountReference extends RejectReason {
    @Getter
    private final AccountAddress address;

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_ACCOUNT_REFERENCE;
    }
}
