package com.concordium.sdk.responses.transactionstatus;


import com.concordium.sdk.types.AccountAddress;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public final class AmountAddedByDecryptionResult implements TransactionResultEvent {
    private final String amount;
    private final AccountAddress account;
    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.AMOUNT_ADDED_BY_DECRYPTION;
    }
}
