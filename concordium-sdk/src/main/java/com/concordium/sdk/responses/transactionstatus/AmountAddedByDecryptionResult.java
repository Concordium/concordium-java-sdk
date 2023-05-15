package com.concordium.sdk.responses.transactionstatus;


import com.concordium.grpc.v2.AccountTransactionEffects;
import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

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

    // TODO
    public static AmountAddedByDecryptionResult parse(AccountTransactionEffects.TransferredToPublic transferredToPublic) {
        return null;
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.AMOUNT_ADDED_BY_DECRYPTION;
    }
}
