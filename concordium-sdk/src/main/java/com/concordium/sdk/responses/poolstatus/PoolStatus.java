package com.concordium.sdk.responses.poolstatus;

import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import concordium.ConcordiumP2PRpc;
import lombok.*;

import java.util.Optional;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "poolType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = BakerPoolStatus.class, name = "BakerPool"),
        @JsonSubTypes.Type(value = PassiveDelegationStatus.class, name = "PassiveDelegation")
})
public abstract class PoolStatus {
    public static Optional<PoolStatus> fromJson(ConcordiumP2PRpc.JsonResponse res) {
        try {
            val poolStatus = JsonMapper.INSTANCE.readValue(res.getValue(), PoolStatus.class);
            return Optional.ofNullable(poolStatus);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse PoolStatus JSON", e);
        }
    }
}
