package com.concordium.sdk.examples.contractexample.cis2nft;

import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.parameters.ListParam;

import java.util.List;

public class NftUpdateOperatorParams extends ListParam {
    public NftUpdateOperatorParams(Schema cis2nftSchema, ReceiveName updateOperatorReceiveName, List<UpdateOperator> updateOperatorList) {
        super(cis2nftSchema, updateOperatorReceiveName, updateOperatorList);
    }
}
