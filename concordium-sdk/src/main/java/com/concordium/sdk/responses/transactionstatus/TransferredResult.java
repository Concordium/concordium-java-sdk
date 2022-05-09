package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.GTUAmount;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;
import java.util.Objects;

@Getter
@ToString
public final class TransferredResult extends TransactionResultEvent {
    private final AbstractAccount to;
    private final AbstractAccount from;
    private GTUAmount amount;

    @JsonCreator
    TransferredResult(@JsonProperty("to") Map<String, Object> to,
                      @JsonProperty("from") Map<String, Object> from,
                      @JsonProperty("amount") String amount) {

        this.to = AbstractAccount.parseAccount(to);
        this.from = AbstractAccount.parseAccount(from);
        if (!Objects.isNull(amount)) {
            this.amount = GTUAmount.fromMicro(amount);
        }
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.TRANSFERRED;
    }
}
