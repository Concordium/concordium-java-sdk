package com.concordium.sdk.responses.token;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class TokenTotalSupply {
    private Integer decimals;
    private Long value;
}
