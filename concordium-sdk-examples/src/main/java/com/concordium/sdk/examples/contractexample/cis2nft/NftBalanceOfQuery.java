package com.concordium.sdk.examples.contractexample.cis2nft;

import com.concordium.sdk.types.AbstractAddress;

/**
 * Wrapper class for {@link BalanceOfQuery} enforcing the correct Token id for cis2-nft contract.
 */
public class NftBalanceOfQuery extends BalanceOfQuery<TokenIdU32> {

    public NftBalanceOfQuery(TokenIdU32 tokenId, AbstractAddress address) {
        super(tokenId, address);
    }
}
