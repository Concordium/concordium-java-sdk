package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.EncryptedSelfAmountAddedEvent;
import com.concordium.grpc.v2.NewEncryptedAmountEvent;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.transactions.EncryptedAmount;
import com.concordium.sdk.types.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * The sender has transferred funds from public to encrypted balance.
 */
@Getter
@ToString
@Builder
@EqualsAndHashCode(callSuper = true)
public final class EncryptedSelfAmountAddedResult extends TransactionResultEvent {
    private final CCDAmount amount;
    private final AccountAddress account;
    private final EncryptedAmount newAmount;

    @JsonCreator
    EncryptedSelfAmountAddedResult(@JsonProperty("amount") String amount,
                                   @JsonProperty("account") AccountAddress account,
                                   @JsonProperty("newAmount") String newAmount) {
        this.amount = CCDAmount.fromMicro(amount);
        this.account = account;
        this.newAmount = EncryptedAmount.from(newAmount);
    }

    public static EncryptedSelfAmountAddedResult from(EncryptedSelfAmountAddedEvent added) {
        return EncryptedSelfAmountAddedResult
                .builder()
                .account(AccountAddress.from(added.getAccount()))
                .amount(CCDAmount.from(added.getAmount()))
                .newAmount(EncryptedAmount.from(added.getNewAmount()))
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.ENCRYPTED_SELF_AMOUNT_ADDED;
    }
}
