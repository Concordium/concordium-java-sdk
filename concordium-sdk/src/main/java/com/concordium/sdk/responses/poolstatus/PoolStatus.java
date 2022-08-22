package com.concordium.sdk.responses.poolstatus;

import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import concordium.ConcordiumP2PRpc;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

@Data
@Getter
@ToString
public class PoolStatus {

    private PoolType poolType;

    public static PoolStatus fromJson(ConcordiumP2PRpc.JsonResponse res) {
        try {
            val basePoolStatus = JsonMapper.INSTANCE.readValue(res.getValue(), PoolStatus.class);

            switch (basePoolStatus.getPoolType()) {
                case BAKER_POOL:
                    return BakerPoolStatus.fromJson(res);
                case PASSIVE_DELEGATION:
                    return PassiveDelegationStatus.fromJson(res);
                default:
                    throw new IllegalArgumentException(
                            String.format("Invalid Pool Type: %s", basePoolStatus.getPoolType().name()));
            }
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse PoolStatus JSON", e);
        }
    }
}
