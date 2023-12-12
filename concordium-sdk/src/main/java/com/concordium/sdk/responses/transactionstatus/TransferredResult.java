package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.AccountTransactionEffects;
import com.concordium.sdk.responses.smartcontracts.ContractTraceElement;
import com.concordium.sdk.responses.smartcontracts.ContractTraceElementType;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.transactions.Memo;
import com.concordium.sdk.types.AbstractAddress;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.ContractAddress;
import lombok.*;

import java.util.Optional;

@Getter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public final class TransferredResult implements TransactionResultEvent, ContractTraceElement {

    /**
     * Receiver of the transaction.
     */
    private final AbstractAddress to;

    /**
     * Sender of the transaction.
     */
    private final AbstractAddress from;

    /**
     * The amount sent from "from".
     */
    private CCDAmount amount;

    /**
     * A memo if the transfer was with a memo.
     */
    private Memo memo;

    public Optional<Memo> getMemo() {
        return Optional.ofNullable(this.memo);
    }

    public static TransferredResult from(AccountTransactionEffects.AccountTransfer accountTransfer, AccountAddress sender) {
        TransferredResultBuilder builder = TransferredResult
                .builder()
                .from(sender)
                .to(AccountAddress.from(accountTransfer.getReceiver()))
                .amount(CCDAmount.from(accountTransfer.getAmount()));
        if (accountTransfer.hasMemo()) {
            builder.memo(Memo.from(accountTransfer.getMemo()));
        }
        return builder
                .build();
    }


    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.TRANSFERRED;
    }

    public static TransferredResult from(com.concordium.grpc.v2.ContractTraceElement.Transferred transferred) {
        return TransferredResult
                .builder()
                .from(ContractAddress.from(transferred.getSender()))
                .to(AccountAddress.from(transferred.getReceiver()))
                .amount(CCDAmount.from(transferred.getAmount()))
                .build();
    }

    @Override
    public ContractTraceElementType getTraceType() {
        return ContractTraceElementType.TRANSFERRED;
    }
}
