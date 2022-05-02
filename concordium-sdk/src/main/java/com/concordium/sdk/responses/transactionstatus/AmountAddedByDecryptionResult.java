package com.concordium.sdk.responses.transactionstatus;


import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
public final class AmountAddedByDecryptionResult extends TransactionResultEvent {
    private final String amount;
    private final AccountAddress account;

    @JsonCreator
    AmountAddedByDecryptionResult(@JsonProperty("amount") String amount,
                                  @JsonProperty("account") AccountAddress account) {
        this.amount = amount;
        this.account = account;

    }
}
