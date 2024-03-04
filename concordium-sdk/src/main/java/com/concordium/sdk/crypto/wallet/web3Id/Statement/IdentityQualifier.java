package com.concordium.sdk.crypto.wallet.web3Id.Statement;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Getter;

@Getter
@JsonTypeName("cred")
public class IdentityQualifier extends IdQualifier {
    private List<Long> issuers;
}
