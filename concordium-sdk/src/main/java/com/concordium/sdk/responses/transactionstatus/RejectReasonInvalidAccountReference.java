package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

public class RejectReasonInvalidAccountReference extends RejectReasonContent {
    @Getter
    private final AccountAddress address;

    @JsonCreator
    RejectReasonInvalidAccountReference(String address) {
        this.address = AccountAddress.from(address);
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_ACCOUNT_REFERENCE;
    }
}
