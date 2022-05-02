package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
public final class EncryptedSelfAmountAddedResult extends TransactionResultEvent {
    private final String amount;
    private final AccountAddress account;
    private final String newAmount;

    @JsonCreator
    EncryptedSelfAmountAddedResult(@JsonProperty("amount") String amount,
                                   @JsonProperty("account") AccountAddress account,
                                   @JsonProperty("newAmount") String newAmount) {
        this.amount = amount;
        this.account = account;
        this.newAmount = newAmount;
    }
}
