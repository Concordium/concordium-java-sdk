package com.concordium.sdk.crypto.wallet;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class IdentityRequestInput {

    private IdentityRequestCommon common;    
    private String idCredSec;
    private String prfKey;
    private String blindingRandomness;

}

