package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.AbstractAddress;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * When the sender did not have enough funds to cover his transfer.
 */
@Getter
@ToString
@Builder
public class RejectReasonAmountTooLarge extends RejectReason {

    /**
     * The sender of the transaction.
     */
    private final AbstractAddress account;
    /**
     * The transfer amount.
     */
    private final CCDAmount amount;

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.AMOUNT_TOO_LARGE;
    }
}
