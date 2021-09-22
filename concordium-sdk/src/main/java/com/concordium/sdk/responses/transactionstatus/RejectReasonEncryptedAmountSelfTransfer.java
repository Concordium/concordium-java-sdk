package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RejectReasonEncryptedAmountSelfTransfer extends RejectReason {
    private final AccountAddress address;

    @JsonCreator
    RejectReasonEncryptedAmountSelfTransfer(@JsonProperty("contents") String address) {
        this.address = AccountAddress.from(address);
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.ENCRYPTED_AMOUNT_SELF_TRANSFER;
    }
}
