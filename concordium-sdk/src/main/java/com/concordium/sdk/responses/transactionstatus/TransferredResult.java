package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.AccountTransactionEffects;
import com.concordium.sdk.responses.transactionevent.accounttransactionresults.ContractTraceElement;
import com.concordium.sdk.responses.transactionevent.accounttransactionresults.ContractTraceElementType;
import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.AbstractAddress;
import com.concordium.sdk.types.Account;
import com.concordium.sdk.types.ContractAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;
import java.util.Objects;

/**
 * A simple account to account transfer or contract to account transfer occurred.
 * This is the result of a successful Transfer transaction.
 */
@Getter
@ToString
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public final class TransferredResult implements TransactionResultEvent, ContractTraceElement {

    /**
     * Receiver account.
     */
    private final AccountAddress to;
    /**
     * Sender. Is either a {@link ContractAddress} or {@link Account}
     */
    private final AbstractAddress from;
    /**
     * Amount transferred.
     */
    private CCDAmount amount;

    @JsonCreator
    TransferredResult(@JsonProperty("to") AccountAddress to,
                      @JsonProperty("from") Map<String, Object> from,
                      @JsonProperty("amount") String amount) {

        this.to = to;
        this.from = AbstractAddress.parseAccount(from);
        if (!Objects.isNull(amount)) {
            this.amount = CCDAmount.fromMicro(amount);
        }
    }

    /**
     * Parses {@link com.concordium.grpc.v2.ContractTraceElement.Transferred} to {@link TransferredResult}.
     * @param transferred {@link com.concordium.grpc.v2.ContractTraceElement.Transferred} returned by the GRPC V2 API.
     * @return parsed {@link TransferredResult}
     */
    public static TransferredResult parse(com.concordium.grpc.v2.ContractTraceElement.Transferred transferred) {
        return TransferredResult.builder()
                .to(AccountAddress.parse(transferred.getReceiver()))
                .from(ContractAddress.parse(transferred.getSender()))
                .amount(CCDAmount.fromMicro(transferred.getAmount().getValue()))
                .build();
    }

    /**
     * Parses {@link AccountTransactionEffects.AccountTransfer} and {@link com.concordium.grpc.v2.AccountAddress}to {@link TransferredResult}.
     * @param accountTransfer {@link AccountTransactionEffects.AccountTransfer} returned by the GRPC V2 API.
     * @param sender {@link com.concordium.grpc.v2.AccountAddress} returned by the GRPC V2 API.
     * @return parsed {@link TransferredResult}.
     */
    public static TransferredResult parse(AccountTransactionEffects.AccountTransfer accountTransfer, com.concordium.grpc.v2.AccountAddress sender) {
        return TransferredResult.builder()
                .to(AccountAddress.parse(accountTransfer.getReceiver()))
                .from(Account.parse(sender))
                .amount(CCDAmount.fromMicro(accountTransfer.getAmount().getValue()))
                .build();

    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.TRANSFERRED;
    }

    @Override
    public ContractTraceElementType getTraceType() {
        return ContractTraceElementType.TRANSFERRED;
    }
}
