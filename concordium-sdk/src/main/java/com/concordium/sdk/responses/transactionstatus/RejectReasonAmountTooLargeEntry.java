package com.concordium.sdk.responses.transactionstatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
class RejectReasonAmountTooLargeEntry extends RejectReasonContent {
    private final List<RejectReasonAmountTooLargeEntry> entries = new ArrayList<>();

    public void add(RejectReasonAmountTooLargeEntry entry) {
        entries.add(entry);
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.AMOUNT_TOO_LARGE;
    }
}
