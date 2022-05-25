package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;
import java.util.Objects;

@Getter
@ToString
public final class TransferredResult extends TransactionResultEvent {
    private final AbstractAddress to;
    private final AbstractAddress from;
    private CCDAmount amount;

    @JsonCreator
    TransferredResult(@JsonProperty("to") Map<String, Object> to,
                      @JsonProperty("from") Map<String, Object> from,
                      @JsonProperty("amount") String amount) {

        this.to = AbstractAddress.parseAccount(to);
        this.from = AbstractAddress.parseAccount(from);
        if (!Objects.isNull(amount)) {
            this.amount = CCDAmount.fromMicro(amount);
        }
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.TRANSFERRED;
    }
}
