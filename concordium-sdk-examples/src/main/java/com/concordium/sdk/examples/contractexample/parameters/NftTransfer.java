package com.concordium.sdk.examples.contractexample.parameters;

import com.concordium.sdk.types.AbstractAddress;
import com.concordium.sdk.types.UInt8;

import java.util.List;

/**
 * Wrapper class for {@link Transfer} enforcing the correct Token amount/id for cis2-nft contract.
 */
public class NftTransfer extends Transfer<TokenIdU32, TokenAmountU8> {

    public NftTransfer(TokenIdU32 tokenId, TokenAmountU8 amount, AbstractAddress from, Receiver to, List<UInt8> data) {
        super(tokenId, amount, from, to, data);
    }
}
