package com.concordium.sdk.responses.blockitemsummary;

import com.concordium.grpc.v2.AccountTransactionEffects;
import com.concordium.sdk.responses.transactionstatus.EncryptedAmountsRemovedResult;
import com.concordium.sdk.responses.transactionstatus.NewEncryptedAmountResult;
import com.concordium.sdk.transactions.Memo;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;
import java.util.Optional;

@Builder
@Getter
@EqualsAndHashCode
@ToString(doNotUseGetters = true)
public class EncryptedTransferResult {

    /**
     * Event on the sender account.
     */
    private final EncryptedAmountsRemovedResult sender;

    /**
     * Event on the receiver account.
     */
    private final NewEncryptedAmountResult receiver;

    /**
     * There may be a memo in the encrypted transfer.
     * Check if it's present with {@link EncryptedTransferResult#hasMemo()}
     */
    private final Memo memo;

    public Optional<Memo> getMemo() {
        return Optional.ofNullable(memo);
    }

    public boolean hasMemo() {
        return (!Objects.isNull(this.memo));
    }

    public static EncryptedTransferResult from(AccountTransactionEffects.EncryptedAmountTransferred encryptedAmountTransferred) {
        EncryptedTransferResultBuilder builder = EncryptedTransferResult
                .builder()
                .sender(EncryptedAmountsRemovedResult.from(encryptedAmountTransferred.getRemoved()))
                .receiver(NewEncryptedAmountResult.from(encryptedAmountTransferred.getAdded()));

        if (encryptedAmountTransferred.hasMemo()) {
            builder.memo(Memo.from(encryptedAmountTransferred.getMemo()));
        }
        return builder.build();
    }


}
