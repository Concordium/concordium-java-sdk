package com.concordium.sdk.examples.contractexample.cis2nft;

import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.parameters.ListParam;

import java.util.List;

public class NftSupportsQueryParams extends ListParam {
    public NftSupportsQueryParams(Schema cis2nftSchema, ReceiveName supportsReceiveName, List<String> identifiers) {
        super(cis2nftSchema, supportsReceiveName, identifiers);
    }
}
