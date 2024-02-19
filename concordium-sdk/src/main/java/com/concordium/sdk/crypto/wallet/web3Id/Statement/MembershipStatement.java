package com.concordium.sdk.crypto.wallet.web3Id.Statement;

import java.util.List;

import com.concordium.sdk.crypto.wallet.web3Id.CredentialAttribute;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Getter;

@Getter
@JsonTypeName("AttributeInSet")
public class MembershipStatement extends AtomicStatement {
    private String attributeTag;
    private List<CredentialAttribute> set;
}
