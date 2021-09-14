package com.concordium.sdk.responsetypes.accountinfo.credential;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
class Policy {
    private String createdAt;
    private String validTo;
    private Attributes revealedAttributes;
}
