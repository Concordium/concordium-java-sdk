package com.concordium.sdk.responsetypes.accountinfo.credential;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
class CredentialPublicKeys {
    private Map<String, Key> keys;
    private int threshold;
}
