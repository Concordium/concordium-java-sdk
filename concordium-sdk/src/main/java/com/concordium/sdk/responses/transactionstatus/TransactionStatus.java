package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.Hash;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
public final class TransactionStatus {
    private final Status status;
    private final Map<Hash, TransactionSummary> outcomes;

    @JsonCreator
    TransactionStatus(@JsonProperty("status") Status status,
                      @JsonProperty("outcomes") Map<Hash, TransactionSummary> outcomes) {
        this.status = status;
        this.outcomes = outcomes;
    }

    public static TransactionStatus fromJson(String transactionStatus) {
        try {
            return JsonMapper.INSTANCE.readValue(transactionStatus, TransactionStatus.class);
        } catch (JsonProcessingException e) {
            System.out.println(transactionStatus);
            throw new IllegalArgumentException("Cannot parse TransactionStatus JSON", e);
        }
    }
}
