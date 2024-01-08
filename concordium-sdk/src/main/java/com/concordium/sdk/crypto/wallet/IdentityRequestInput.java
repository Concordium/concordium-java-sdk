package com.concordium.sdk.crypto.wallet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class IdentityRequestInput {

    private IdentityRequestCommon common;    
    private String idCredSec;
    private String prfKey;
    private String blindingRandomness;

}

