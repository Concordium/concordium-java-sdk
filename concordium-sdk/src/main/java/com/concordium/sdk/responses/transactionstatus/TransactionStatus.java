package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.Hash;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;

@Getter
@ToString
@Builder
@Jacksonized
public final class TransactionStatus {
    @JsonProperty("status")
    private final Status status;
    @JsonProperty("outcomes")
    private final Map<Hash, TransactionSummary> outcomes;

    public static TransactionStatus fromJson(String transactionStatus) {
        try {
            return JsonMapper.INSTANCE.readValue(transactionStatus, TransactionStatus.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse TransactionStatus JSON", e);
        }
    }
}
