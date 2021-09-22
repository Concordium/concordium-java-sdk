package com.concordium.sdk.responses.transactionstatus;

import lombok.Getter;
import lombok.ToString;

@ToString
public class RejectReasonAmountTooLargeEntryAccount extends RejectReasonAmountTooLargeEntry {
    @Getter
    private final AbstractAccount account;

    RejectReasonAmountTooLargeEntryAccount(AbstractAccount abstractAccount) {
        this.account = abstractAccount;
    }
}
