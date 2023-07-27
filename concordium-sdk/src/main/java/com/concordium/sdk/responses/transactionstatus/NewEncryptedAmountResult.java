package com.concordium.sdk.responses.transactionstatus;


import com.concordium.grpc.v2.NewEncryptedAmountEvent;
import com.concordium.sdk.transactions.EncryptedAmount;
import com.concordium.sdk.types.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * When an account receives an encrypted amount.
 */
@Getter
@ToString
@Builder
@EqualsAndHashCode(callSuper = true)
public final class NewEncryptedAmountResult extends TransactionResultEvent {
    private final AccountAddress account;
    private final long newIndex;
    private final EncryptedAmount encryptedAmount;


    @JsonCreator
    NewEncryptedAmountResult(@JsonProperty("account") AccountAddress account,
                             @JsonProperty("newIndex") long newIndex,
                             @JsonProperty("encryptedAmount") String encryptedAmount) {

        this.account = account;
        this.newIndex = newIndex;
        this.encryptedAmount = EncryptedAmount.from(encryptedAmount);
    }

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
