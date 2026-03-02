package com.concordium.sdk.responses.token;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class TokenModuleState {

    private Boolean allowList;
    private Boolean denyList;
    private Boolean mintable;
    private String name;
    private Boolean paused;

    //TODO: governanceAccount
    //TODO: metadata
}
