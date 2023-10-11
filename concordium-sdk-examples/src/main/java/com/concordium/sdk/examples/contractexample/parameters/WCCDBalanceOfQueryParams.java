package com.concordium.sdk.examples.contractexample.parameters;

import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.parameters.ListParam;

import java.util.List;

public class WCCDBalanceOfQueryParams extends ListParam {
    public WCCDBalanceOfQueryParams(Schema schema, ReceiveName receiveName, List<WCCDBalanceOfQuery> list) {
        super(schema, receiveName, list);
    }
}
