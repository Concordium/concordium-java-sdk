package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.types.AccountAddress;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Account tried to transfer an encrypted amount to itself, that's not allowed.
 */
@Getter
@ToString
@Builder
public class RejectReasonEncryptedAmountSelfTransfer extends RejectReason {
    private final AccountAddress address;

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.ENCRYPTED_AMOUNT_SELF_TRANSFER;
    }
}
