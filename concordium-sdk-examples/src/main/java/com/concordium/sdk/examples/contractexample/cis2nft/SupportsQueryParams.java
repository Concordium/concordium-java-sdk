package com.concordium.sdk.examples.contractexample.cis2nft;

import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.parameters.ListParam;

import java.util.List;

public class SupportsQueryParams extends ListParam {
    public SupportsQueryParams(Schema cis2nftSchema, ReceiveName supportsReceiveName, List<String> identifiers) {
        super(cis2nftSchema, supportsReceiveName, identifiers);
    }
}
