package com.concordium.sdk.responses.transactionstatusinblock;

import com.concordium.sdk.responses.transactionstatus.Status;
import com.concordium.sdk.responses.transactionstatus.TransactionSummary;
import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import concordium.ConcordiumP2PRpc;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.jackson.Jacksonized;
import lombok.val;

import java.util.Optional;

/**
 * The transaction status for a specific transaction in a specific block.
 */
@Data
@Jacksonized
@Builder
@EqualsAndHashCode
public class TransactionStatusInBlock {

    /**
     * Status of the transaction.
     */
    private final Status status;

    /**
     * Summary of the outcome of a block item.
     */
    private final Optional<TransactionSummary> result;

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
