package com.concordium.sdk.crypto.wallet.web3Id.Statement;

import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Getter;

@Getter
@JsonTypeName("RevealAttribute")
public class RevealStatement extends AtomicStatement {
    private String attributeTag;
}
