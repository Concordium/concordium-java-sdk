package com.concordium.sdk.responses.transactionstatus;


import com.concordium.grpc.v2.NewEncryptedAmountEvent;
import com.concordium.sdk.transactions.EncryptedAmount;
import com.concordium.sdk.types.AccountAddress;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * When an account receives an encrypted amount.
 */
@Getter
@ToString
@EqualsAndHashCode
@Builder
public final class NewEncryptedAmountResult implements TransactionResultEvent {
    private final AccountAddress account;
    private final long newIndex;
    private final EncryptedAmount encryptedAmount;

    public static NewEncryptedAmountResult from(NewEncryptedAmountEvent added) {
        return NewEncryptedAmountResult
                .builder()
                .account(AccountAddress.from(added.getReceiver()))
                .newIndex(added.getNewIndex())
                .encryptedAmount(EncryptedAmount.from(added.getEncryptedAmount()))
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.NEW_ENCRYPTED_AMOUNT;
    }
}
