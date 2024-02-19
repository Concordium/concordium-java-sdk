package com.concordium.sdk.crypto.wallet.web3Id.Statement;

import com.concordium.sdk.crypto.wallet.web3Id.CredentialAttribute;
import com.concordium.sdk.responses.accountinfo.credential.AttributeType;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@JsonTypeName("AttributeInRange")
@Getter
@Builder
@Jacksonized
public class RangeStatement extends AtomicStatement {
    private CredentialAttribute lower;
    private CredentialAttribute upper;
    private String attributeTag;
}
