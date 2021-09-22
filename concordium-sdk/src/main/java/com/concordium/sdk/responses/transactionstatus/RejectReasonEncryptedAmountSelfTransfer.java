package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class RejectReasonEncryptedAmountSelfTransfer extends RejectReasonContent {
    private final AccountAddress address;

    @JsonCreator
    RejectReasonEncryptedAmountSelfTransfer(String address) {
        this.address = AccountAddress.from(address);
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.ENCRYPTED_AMOUNT_SELF_TRANSFER;
    }
}
