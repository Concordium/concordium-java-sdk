package com.concordium.sdk.examples.contractexample.cis2nft;

import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.parameters.ListParam;

import java.util.List;

public class NftBalanceOfQueryParams extends ListParam {
    public NftBalanceOfQueryParams(Schema cis2nftSchema, ReceiveName balanceOfReceiveName, List<NftBalanceOfQuery> balanceOfQueries) {
        super(cis2nftSchema, balanceOfReceiveName, balanceOfQueries);
    }
}
