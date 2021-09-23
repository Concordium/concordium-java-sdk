package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.GTUAmount;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@ToString
public class RejectReasonAmountTooLarge extends RejectReason {

    private final AbstractAccount account;
    private final GTUAmount amount;

    @JsonCreator
    RejectReasonAmountTooLarge(@JsonProperty("contents") List<Object> contents) {
        if (contents.size() != 2) {
            throw new IllegalArgumentException("Cannot parse AmountTooLarge. Unexpected array length.");
        }
        try {
            this.account = AbstractAccount.parseAccount((Map<String, Object>) contents.get(0));
            this.amount = GTUAmount.fromMicro((String) contents.get(1));
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Cannot parse AmountTooLarge. Unexpected type.", e);
        }
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.AMOUNT_TOO_LARGE;
    }
}
