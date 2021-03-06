package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public final class TransferredWithScheduleResult extends TransactionResultEvent {
    private final List<List<String>> amount;
    private final AccountAddress to;
    private final AccountAddress from;

    @JsonCreator
    TransferredWithScheduleResult(@JsonProperty("amount") List<List<String>> amount,
                                  @JsonProperty("to") AccountAddress to,
                                  @JsonProperty("from") AccountAddress from) {
        this.amount = amount;
        this.to = to;
        this.from = from;
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.TRANSFERRED_WITH_SCHEDULE;
    }
}
