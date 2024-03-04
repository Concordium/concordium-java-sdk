package com.concordium.sdk.crypto.wallet.web3Id.Statement;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum StatementType {
    @JsonProperty("cred")
    Credential,
    @JsonProperty("sci")
    SmartContract
}
