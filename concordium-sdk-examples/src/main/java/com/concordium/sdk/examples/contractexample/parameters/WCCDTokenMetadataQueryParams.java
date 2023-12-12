package com.concordium.sdk.examples.contractexample.parameters;

import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.parameters.ListParam;

import java.util.List;

/**
 * Wrapper class for {@link ListParam} enforcing the correct {@link TokenId} for cis2-wccd contract.
 */
public class WCCDTokenMetadataQueryParams extends ListParam {
    public WCCDTokenMetadataQueryParams(Schema schema, ReceiveName receiveName, List<TokenIdUnit> list) {
        super(schema, receiveName, list);
    }
}
