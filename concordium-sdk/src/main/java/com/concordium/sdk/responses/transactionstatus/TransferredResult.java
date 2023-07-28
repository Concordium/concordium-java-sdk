package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.AccountTransactionEffects;
import com.concordium.sdk.responses.smartcontracts.ContractTraceElement;
import com.concordium.sdk.responses.smartcontracts.ContractTraceElementType;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.AbstractAddress;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.ContractAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;
import java.util.Objects;

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

    @JsonCreator
    TransferredResult(@JsonProperty("to") Map<String, Object> to,
                      @JsonProperty("from") Map<String, Object> from,
                      @JsonProperty("amount") String amount) {

        this.to = AbstractAddress.parseAccount(to);
        this.from = AbstractAddress.parseAccount(from);
        if (!Objects.isNull(amount)) {
            this.amount = CCDAmount.fromMicro(amount);
        }
    }

    public static TransferredResult from(AccountTransactionEffects.AccountTransfer accountTransfer, AccountAddress sender) {
        return TransferredResult
                .builder()
                .from(sender)
                .to(AccountAddress.from(accountTransfer.getReceiver()))
                .amount(CCDAmount.from(accountTransfer.getAmount()))
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
