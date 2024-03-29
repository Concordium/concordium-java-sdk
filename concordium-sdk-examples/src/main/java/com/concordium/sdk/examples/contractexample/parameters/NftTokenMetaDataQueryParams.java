package com.concordium.sdk.examples.contractexample.parameters;

import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.parameters.ListParam;

import java.util.List;

/**
 * Wrapper class for {@link ListParam} enforcing the correct Token id for cis2-nft contract.
 */
public class NftTokenMetaDataQueryParams extends ListParam {
    public NftTokenMetaDataQueryParams(Schema cis2nftSchema, ReceiveName tokenMetadataReceiveName, List<TokenIdU32> tokensForMetadataQuery) {
        super(cis2nftSchema, tokenMetadataReceiveName, tokensForMetadataQuery);
    }
}
