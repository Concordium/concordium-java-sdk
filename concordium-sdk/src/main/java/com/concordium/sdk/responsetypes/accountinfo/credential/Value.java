package com.concordium.sdk.responsetypes.accountinfo.credential;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
class Value {
    private Contents contents;
    private String type;
}
