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

    private AbstractAccount account;
    private GTUAmount amount;

    @JsonCreator
    RejectReasonAmountTooLarge(@JsonProperty("contents") List<Object> contents) {
        if (contents.size() != 2) {
            throw new IllegalArgumentException("Cannot parse AmountTooLarge. Unexpected array length.");
        }
        for (Object content : contents) {
            if (content instanceof String) {
                this.amount = GTUAmount.fromMicro((String) content);
            } else if (content instanceof Map) {
                this.account = AbstractAccount.parseAccount((Map<String, Object>) content);
            } else {
                throw new IllegalArgumentException("Cannot parse AmountTooLarge. Unexpected type.");
            }
        }
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.AMOUNT_TOO_LARGE;
    }
}
