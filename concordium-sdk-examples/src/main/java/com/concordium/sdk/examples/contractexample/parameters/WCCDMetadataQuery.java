package com.concordium.sdk.examples.contractexample.parameters;

import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.parameters.ListParam;

import java.util.List;

public class WCCDMetadataQuery extends ListParam {
    public WCCDMetadataQuery(Schema schema, ReceiveName receiveName, List<TokenIdUnit> list) {
        super(schema, receiveName, list);
    }
}