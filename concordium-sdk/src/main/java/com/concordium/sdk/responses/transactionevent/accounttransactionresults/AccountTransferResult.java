package com.concordium.sdk.responses.transactionevent.accounttransactionresults;

import com.concordium.grpc.v2.AccountTransactionEffects;
import com.concordium.sdk.responses.transactionstatus.TransactionResultEventType;
import com.concordium.sdk.responses.transactionstatus.TransferMemoResult;
import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.CCDAmount;
import lombok.*;

import java.util.Objects;
import java.util.Optional;

/**
 * A simple account to account transfer occurred.
 * This is the result of a successful Transfer transaction
 */
@Builder
@EqualsAndHashCode
@ToString
public class AccountTransferResult implements AccountTransactionResult {

    private TransactionResultEventType type;

    /**
     * Amount that was transferred.
     */
    @Getter
    private CCDAmount amount;

    /**
     * Receiver account.
     */
    @Getter
    private AccountAddress receiver;

    /**
     * Only present if type is {@link TransactionResultEventType#TRANSFER_WITH_MEMO}
     */
    private TransferMemoResult memo;

    public static AccountTransferResult parse(AccountTransactionEffects.AccountTransfer accountTransfer) {

        val memo = accountTransfer.hasMemo()
                ? TransferMemoResult.parse(accountTransfer.getMemo())
                : null;

        val type = accountTransfer.hasMemo()
                ? TransactionResultEventType.TRANSFERRED
                : TransactionResultEventType.TRANSFER_WITH_MEMO;

        return AccountTransferResult.builder()
                .type(type)
                .amount(CCDAmount.fromMicro(accountTransfer.getAmount().getValue()))
                .receiver(AccountAddress.parse(accountTransfer.getReceiver()))
                .memo(memo)
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return type;
    }

    public Optional<TransferMemoResult> getMemo() {
        return Objects.isNull(memo)
                ? Optional.empty()
                : Optional.of(memo);
    }
}
