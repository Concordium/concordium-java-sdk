package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.ContractTraceElement;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.AbstractAddress;
import com.concordium.sdk.types.Account;
import com.concordium.sdk.types.ContractAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;
import java.util.Objects;

@Getter
@ToString
@Builder
public final class TransferredResult extends TransactionResultEvent {
    private final AbstractAddress to;
    private final AbstractAddress from;
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

    /**
     * Parses {@link com.concordium.grpc.v2.ContractTraceElement.Transferred} to {@link TransferredResult}.
     * @param transferred {@link com.concordium.grpc.v2.ContractTraceElement.Transferred} returned by the GRPC V2 API.
     * @return parsed {@link TransferredResult}
     */
    public static TransferredResult parse(ContractTraceElement.Transferred transferred) {
        return TransferredResult.builder()
                .to(Account.parse(transferred.getReceiver()))
                .from(ContractAddress.parse(transferred.getSender()))
                .amount(CCDAmount.fromMicro(transferred.getAmount().getValue()))
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.TRANSFERRED;
    }
}
