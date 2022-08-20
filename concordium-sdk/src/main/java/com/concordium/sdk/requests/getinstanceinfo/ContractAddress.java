package com.concordium.sdk.requests.getinstanceinfo;

import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Builder;
import lombok.Getter;

/**
 * Smart Contract Address.
 */
@Builder
@Getter
public class ContractAddress {
    private final long index;
    private final long subindex;

    public String toJson() throws JsonProcessingException {
        return JsonMapper.INSTANCE.writeValueAsString(this);
    }
}
