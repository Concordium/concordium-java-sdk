package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
public final class NewEncryptedAmountResult extends TransactionResultEvent {
    private final AccountAddress account;
    private final String newIndex;
    private final String encryptedAmount;


    @JsonCreator
    NewEncryptedAmountResult(@JsonProperty("account") AccountAddress account,
                             @JsonProperty("newIndex") String newIndex,
                             @JsonProperty("encryptedAmount") String encryptedAmount) {

        this.account = account;
        this.newIndex = newIndex;
        this.encryptedAmount = encryptedAmount;
    }
}
