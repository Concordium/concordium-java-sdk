package com.concordium.sdk.responses.transactionevent.accounttransactionresults;

import com.concordium.grpc.v2.AccountTransactionEffects.EncryptedAmountTransferred;
import com.concordium.sdk.responses.transactionstatus.EncryptedAmountsRemovedResult;
import com.concordium.sdk.responses.transactionstatus.NewEncryptedAmountResult;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * An encrypted amount was transferred.
 * This is the result of a successful EncryptedAmountTransfer transaction.
 */
@SuperBuilder
@ToString
@Getter
@EqualsAndHashCode
public class EncryptedAmountTransferredResult implements AccountTransactionResult{


    private EncryptedAmountsRemovedResult removed;

    private NewEncryptedAmountResult added;

    /**
     * Parses {@link EncryptedAmountTransferred} to {@link EncryptedAmountTransferredResult}.
     * @param encryptedAmountTransferred {@link EncryptedAmountTransferred} returned by the GRPC V2 API.
     * @return parsed {@link EncryptedAmountTransferredResult}
     */
    public static EncryptedAmountTransferredResult parse(EncryptedAmountTransferred encryptedAmountTransferred) {

        return EncryptedAmountTransferredResult.builder()
                .removed(EncryptedAmountsRemovedResult.parse(encryptedAmountTransferred.getRemoved()))
                .added(NewEncryptedAmountResult.parse(encryptedAmountTransferred.getAdded()))
                .build();
    }

    @Override
    public TransactionType getResultType() {
        return TransactionType.ENCRYPTED_AMOUNT_TRANSFER;
    }
}
