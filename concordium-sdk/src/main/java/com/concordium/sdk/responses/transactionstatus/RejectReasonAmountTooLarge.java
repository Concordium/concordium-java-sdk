package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.List;

@Getter
public class RejectReasonAmountTooLarge extends RejectReasonContent {

    private final List<RejectReasonAmountTooLargeEntry> entries;


    @JsonCreator
    RejectReasonAmountTooLarge(List<RejectReasonAmountTooLargeEntry> entries) {
        this.entries = entries;
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.AMOUNT_TOO_LARGE;
    }

}
