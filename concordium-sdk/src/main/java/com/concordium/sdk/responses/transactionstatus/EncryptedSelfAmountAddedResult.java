package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.EncryptedSelfAmountAddedEvent;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.transactions.EncryptedAmount;
import com.concordium.sdk.types.AccountAddress;
import lombok.*;

/**
 * The sender has transferred funds from public to encrypted balance.
 */
@Getter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public final class EncryptedSelfAmountAddedResult implements TransactionResultEvent {
    private final CCDAmount amount;
    private final AccountAddress account;
    private final EncryptedAmount newAmount;

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
