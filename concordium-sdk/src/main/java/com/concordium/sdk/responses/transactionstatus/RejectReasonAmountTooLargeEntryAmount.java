package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.GTUAmount;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RejectReasonAmountTooLargeEntryAmount extends RejectReasonAmountTooLargeEntry {
    private final GTUAmount amount;

    RejectReasonAmountTooLargeEntryAmount(GTUAmount gtuAmount) {
        super();
        this.amount = gtuAmount;
    }
}
