package com.concordium.sdk.responsetypes.accountinfo.credential;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Credential {
    @JsonProperty("v")
    private String version;
    private Value value;

}
