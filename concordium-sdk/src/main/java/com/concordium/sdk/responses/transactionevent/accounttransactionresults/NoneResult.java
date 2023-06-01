package com.concordium.sdk.responses.transactionevent.accounttransactionresults;

import com.concordium.grpc.v2.AccountTransactionEffects;
import com.concordium.sdk.responses.transactionstatus.RejectReason;
import lombok.*;

/**
 * No effects other than payment from this transaction.
 */
@Builder
@EqualsAndHashCode
@Getter
@ToString
public class NoneResult implements AccountTransactionResult {

    /**
     * Type of the failed transaction.
     * In case of serialization failure is {@link TransactionType#NOT_KNOWN}.
     */
    private TransactionType failedTransactionType;

    /**
     * Reason for rejection of the transaction.
     */
    private RejectReason rejectReason;

    /**
     * Parses {@link com.concordium.grpc.v2.AccountTransactionEffects.None} to {@link NoneResult}.
     * @param none {@link com.concordium.grpc.v2.AccountTransactionEffects.None} returned by the GRPC V2 API.
     * @return parsed {@link NoneResult}.
     */
    public static NoneResult parse(AccountTransactionEffects.None none) {
        val builder = NoneResult.builder();
        if (none.hasTransactionType()) {
            builder.failedTransactionType(TransactionType.parse(none.getTransactionType()));
        } else {
            builder.failedTransactionType(TransactionType.NOT_KNOWN);
        }
        return builder
                .rejectReason(RejectReason.parse(none.getRejectReason()))
                .build();
    }

    @Override
    public TransactionType getResultType() {
        return TransactionType.NONE;
    }
}
