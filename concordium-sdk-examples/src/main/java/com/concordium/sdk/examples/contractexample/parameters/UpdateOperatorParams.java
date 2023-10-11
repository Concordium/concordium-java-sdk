package com.concordium.sdk.examples.contractexample.parameters;

import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.parameters.ListParam;

import java.util.List;

public class UpdateOperatorParams extends ListParam {
    public UpdateOperatorParams(Schema cis2nftSchema, ReceiveName updateOperatorReceiveName, List<UpdateOperator> updateOperatorList) {
        super(cis2nftSchema, updateOperatorReceiveName, updateOperatorList);
    }
}
