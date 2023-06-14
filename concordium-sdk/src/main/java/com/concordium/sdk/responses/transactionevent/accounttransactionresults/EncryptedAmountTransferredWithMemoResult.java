package com.concordium.sdk.responses.transactionevent.accounttransactionresults;

import com.concordium.grpc.v2.AccountTransactionEffects;
import com.concordium.sdk.responses.transactionstatus.EncryptedAmountsRemovedResult;
import com.concordium.sdk.responses.transactionstatus.NewEncryptedAmountResult;
import com.concordium.sdk.transactions.Memo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EncryptedAmountTransferredWithMemoResult extends EncryptedAmountTransferredResult {

    private Memo memo;

    /**
     * Parses {@link AccountTransactionEffects.EncryptedAmountTransferred} to {@link EncryptedAmountTransferredWithMemoResult}.
     * @param encryptedAmountTransferred {@link AccountTransactionEffects.EncryptedAmountTransferred} returned by the GRPC V2 API.
     * @return parsed {@link EncryptedAmountTransferredWithMemoResult}
     */
    public static EncryptedAmountTransferredWithMemoResult parse (AccountTransactionEffects.EncryptedAmountTransferred encryptedAmountTransferred) {
        return EncryptedAmountTransferredWithMemoResult.builder()
                .removed(EncryptedAmountsRemovedResult.parse(encryptedAmountTransferred.getRemoved()))
                .added(NewEncryptedAmountResult.parse(encryptedAmountTransferred.getAdded()))
                .memo(Memo.from(encryptedAmountTransferred.getMemo().getValue().toByteArray()))
                .build();
    }
    @Override
    public TransactionType getTransactionType() {
        return TransactionType.ENCRYPTED_AMOUNT_TRANSFER_WITH_MEMO;
    }
}
