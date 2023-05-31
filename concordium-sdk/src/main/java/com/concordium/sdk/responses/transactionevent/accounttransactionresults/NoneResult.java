package com.concordium.sdk.responses.transactionevent.accounttransactionresults;

import com.concordium.grpc.v2.AccountTransactionEffects;
import com.concordium.sdk.responses.transactionstatus.RejectReason;
import com.concordium.sdk.responses.transactionstatus.TransactionResultEventType;
import lombok.*;

@Builder
@EqualsAndHashCode
@Getter
@ToString
public class NoneResult implements AccountTransactionResult {

    private TransactionResultEventType failedTransactionType;

    private RejectReason rejectReason;

    /**
     * Parses {@link com.concordium.grpc.v2.AccountTransactionEffects.None} to {@link NoneResult}.
     * @param none {@link com.concordium.grpc.v2.AccountTransactionEffects.None} returned by the GRPC V2 API.
     * @return parsed {@link NoneResult}.
     */
    public static NoneResult parse(AccountTransactionEffects.None none) {
        val builder = NoneResult.builder();
        if (none.hasTransactionType()) {
            builder.failedTransactionType(TransactionResultEventType.parse(none.getTransactionType()));
        } else {
            builder.failedTransactionType(TransactionResultEventType.NOT_KNOWN);
        }
        return builder
                .rejectReason(RejectReason.parse(none.getRejectReason()))
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.NONE;
    }
}
