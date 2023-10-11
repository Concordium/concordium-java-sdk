package com.concordium.sdk.examples.contractexample.parameters;

import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.parameters.ListParam;

import java.util.List;

/**
 * Wrapper class for {@link ListParam} using {@link OperatorOfQuery}.
 */
public class OperatorOfQueryParams extends ListParam {
    public OperatorOfQueryParams(Schema cis2nftSchema, ReceiveName operatorOfReceiveName, List<OperatorOfQuery> operatorOfQueries) {
        super(cis2nftSchema, operatorOfReceiveName, operatorOfQueries);
    }
}
