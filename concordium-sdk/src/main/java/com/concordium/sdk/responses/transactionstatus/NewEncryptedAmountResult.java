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
    private AccountAddress account;
    private final String newIndex;
    private final String encryptedAmount;


    @JsonCreator
    NewEncryptedAmountResult(@JsonProperty("account") String account,
                             @JsonProperty("newIndex") String newIndex,
                             @JsonProperty("encryptedAmount") String encryptedAmount) {

        if(!Objects.isNull(account)) {
            this.account = AccountAddress.from(account);
        }
        this.newIndex = newIndex;
        this.encryptedAmount = encryptedAmount;
    }
}
