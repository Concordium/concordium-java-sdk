package com.concordium.sdk.responses.transactionstatusinblock;

import com.concordium.sdk.responses.transactionstatus.Status;
import com.concordium.sdk.responses.transactionstatus.TransactionSummary;
import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import concordium.ConcordiumP2PRpc;
import lombok.Getter;
import lombok.val;

import java.util.Optional;

@Getter
public class TransactionStatusInBlock {

    /**
     * Status of the transaction.
     */
    private final Status status;

    /**
     * Summary of the outcome of a block item.
     */
    private final Optional<TransactionSummary> result;

    @JsonCreator
    TransactionStatusInBlock(@JsonProperty("status") Status status) {
        this.status = status;
        this.result = Optional.empty();
    }

    @JsonCreator
    TransactionStatusInBlock(
            @JsonProperty("result") TransactionSummary result,
            @JsonProperty("status") Status status
    ) {
        this.status = status;
        this.result = Optional.of(result);
    }

    public static Optional<TransactionStatusInBlock> fromJson(ConcordiumP2PRpc.JsonResponse res) {
        try {
            val jsonRes = JsonMapper.INSTANCE.readValue(
                    res.getValue(),
                    TransactionStatusInBlock.class);

            return Optional.ofNullable(jsonRes);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse TransactionStatusInBlock JSON", e);
        }
    }
}
