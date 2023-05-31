package com.concordium.sdk.responses.transactionevent.accounttransactionresults;

import com.concordium.grpc.v2.AccountTransactionEffects.EncryptedAmountTransferred;
import com.concordium.sdk.responses.transactionstatus.EncryptedAmountsRemovedResult;
import com.concordium.sdk.responses.transactionstatus.NewEncryptedAmountResult;
import com.concordium.sdk.responses.transactionstatus.TransactionResultEventType;
import com.concordium.sdk.responses.transactionstatus.TransferMemoResult;
import lombok.*;

import java.util.Objects;
import java.util.Optional;

/**
 * An encrypted amount was transferred.
 * This is the result of a successful EncryptedAmountTransfer transaction.
 */
@Builder
@ToString
@EqualsAndHashCode
public class EncryptedAmountTransferredResult implements AccountTransactionResult{

    private TransactionResultEventType type;

    @Getter
    private EncryptedAmountsRemovedResult removed;

    @Getter
    private NewEncryptedAmountResult added;

    /**
     * Only present if type is {@link TransactionResultEventType#ENCRYPTED_AMOUNT_TRANSFERRED_WITH_MEMO}
     */
    private TransferMemoResult memo;

    /**
     * Parses {@link EncryptedAmountTransferred} to {@link EncryptedAmountTransferredResult}.
     * @param encryptedAmountTransferred {@link EncryptedAmountTransferred} returned by the GRPC V2 API.
     * @return parsed {@link EncryptedAmountTransferredResult}
     */
    public static EncryptedAmountTransferredResult parse(EncryptedAmountTransferred encryptedAmountTransferred) {

        val memo = encryptedAmountTransferred.hasMemo()
                ? TransferMemoResult.parse(encryptedAmountTransferred.getMemo())
                : null;

        val type = encryptedAmountTransferred.hasMemo()
                ? TransactionResultEventType.ENCRYPTED_AMOUNT_TRANSFERRED_WITH_MEMO
                : TransactionResultEventType.ENCRYPTED_AMOUNT_TRANSFERRED;

        return EncryptedAmountTransferredResult.builder()
                .type(type)
                .removed(EncryptedAmountsRemovedResult.parse(encryptedAmountTransferred.getRemoved()))
                .added(NewEncryptedAmountResult.parse(encryptedAmountTransferred.getAdded()))
                .memo(memo)
                .build();
    }
    @Override
    public TransactionResultEventType getType() {
        return this.type;
    }

    public Optional<TransferMemoResult> getMemo() {
        return Objects.isNull(memo)
                ? Optional.empty()
                : Optional.of(memo);
    }
}
