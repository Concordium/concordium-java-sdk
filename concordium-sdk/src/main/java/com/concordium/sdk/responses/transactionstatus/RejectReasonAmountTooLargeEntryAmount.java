package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.GTUAmount;
import lombok.Getter;

@Getter
public class RejectReasonAmountTooLargeEntryAmount extends RejectReasonAmountTooLargeEntry {
    private final GTUAmount amount;

    RejectReasonAmountTooLargeEntryAmount(GTUAmount gtuAmount) {
        super();
        this.amount = gtuAmount;
    }
}
