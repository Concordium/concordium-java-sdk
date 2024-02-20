package com.concordium.sdk.crypto.wallet.web3Id.Statement;

import java.util.List;

import com.concordium.sdk.types.ContractAddress;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Getter;

@Getter
@JsonTypeName("sci")
public class Web3IdIssuerQualifier extends IdQualifier {
    private List<ContractAddress> issuers;
}
