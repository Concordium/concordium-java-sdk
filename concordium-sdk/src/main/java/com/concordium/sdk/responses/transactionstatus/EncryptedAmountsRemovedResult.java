package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class EncryptedAmountsRemovedResult extends TransactionResultEvent {
    private final int upToIndex;
    private final AccountAddress account;
    private final String inputAmount;
    private final String newAmount;

    @JsonCreator
    EncryptedAmountsRemovedResult(@JsonProperty("upToIndex") int upToIndex,
                                  @JsonProperty("account") AccountAddress account,
                                  @JsonProperty("inputAmount") String inputAmount,
                                  @JsonProperty("newAmount") String newAmount) {
        this.upToIndex = upToIndex;
        this.account = account;
        this.inputAmount = inputAmount;
        this.newAmount = newAmount;
    }
}
