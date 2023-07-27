package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.types.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * Account tried to transfer an encrypted amount to itself, that's not allowed.
 */
@Getter
@ToString
public class RejectReasonEncryptedAmountSelfTransfer extends RejectReason {
    private final AccountAddress address;

    @JsonCreator
    RejectReasonEncryptedAmountSelfTransfer(@JsonProperty("contents") AccountAddress address) {
        this.address = address;
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.ENCRYPTED_AMOUNT_SELF_TRANSFER;
    }
}
