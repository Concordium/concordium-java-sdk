package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.AbstractAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * When the sender did not have enough funds to cover his transfer.
 */
@Getter
@ToString
@AllArgsConstructor
@Builder
public class RejectReasonAmountTooLarge extends RejectReason {

    /**
     * The sender of the transaction.
     */
    private final AbstractAddress account;
    /**
     * The transfer amount.
     */
    private final CCDAmount amount;

    @JsonCreator
    RejectReasonAmountTooLarge(@JsonProperty("contents") List<Object> contents) {
        if (contents.size() != 2) {
            throw new IllegalArgumentException("Cannot parse AmountTooLarge. Unexpected array length.");
        }
        try {
            this.account = AbstractAddress.parseAccount((Map<String, Object>) contents.get(0));
            this.amount = CCDAmount.fromMicro((String) contents.get(1));
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Cannot parse AmountTooLarge. Unexpected type.", e);
        }
    }

    /**
     * Parses {@link com.concordium.grpc.v2.RejectReason.AmountTooLarge} to {@link RejectReasonAmountTooLarge}.
     * @param amountTooLarge {@link com.concordium.grpc.v2.RejectReason.AmountTooLarge} returned by the GRPC V2 API.
     * @return parsed {@link RejectReasonAmountTooLarge}.
     */
    public static RejectReasonAmountTooLarge parse(com.concordium.grpc.v2.RejectReason.AmountTooLarge amountTooLarge) {
        return RejectReasonAmountTooLarge.builder()
                .account(AbstractAddress.parse(amountTooLarge.getAddress()))
                .amount(CCDAmount.fromMicro(amountTooLarge.getAmount().getValue()))
                .build();
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.AMOUNT_TOO_LARGE;
    }
}
