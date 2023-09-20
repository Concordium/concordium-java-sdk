package com.concordium.sdk.examples.contractexample.cis2nft;

import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.parameters.ListParam;

import java.util.List;

public class NftOperatorOfQueryParams extends ListParam {
    public NftOperatorOfQueryParams(Schema cis2nftSchema, ReceiveName operatorOfReceiveName, List<OperatorOfQuery> operatorOfQueries) {
        super(cis2nftSchema, operatorOfReceiveName, operatorOfQueries);
    }
}
