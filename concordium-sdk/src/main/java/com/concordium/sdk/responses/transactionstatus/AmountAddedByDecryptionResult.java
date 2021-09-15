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
    private AccountAddress account;

    @JsonCreator
    AmountAddedByDecryptionResult(@JsonProperty("amount") String amount,
                                  @JsonProperty("account") String account) {
        this.amount = amount;
        if(!Objects.isNull(account)) {
            this.account = AccountAddress.from(account);
        }
    }
}
