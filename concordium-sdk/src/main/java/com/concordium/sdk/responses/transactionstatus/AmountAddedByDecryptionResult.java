package com.concordium.sdk.responses.transactionstatus;


import com.concordium.sdk.types.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class AmountAddedByDecryptionResult implements TransactionResultEvent {
    private final String amount;
    private final AccountAddress account;

    @JsonCreator
    AmountAddedByDecryptionResult(@JsonProperty("amount") String amount,
                                  @JsonProperty("account") AccountAddress account) {
        this.amount = amount;
        this.account = account;

    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.AMOUNT_ADDED_BY_DECRYPTION;
    }
}
