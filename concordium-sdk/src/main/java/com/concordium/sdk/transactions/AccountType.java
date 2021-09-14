package com.concordium.sdk.transactions;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum AccountType {
    @JsonProperty("AddressAccount")
    ADDRESS_ACCOUNT
}
