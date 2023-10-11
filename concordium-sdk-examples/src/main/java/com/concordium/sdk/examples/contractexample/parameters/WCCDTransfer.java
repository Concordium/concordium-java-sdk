package com.concordium.sdk.examples.contractexample.parameters;

import com.concordium.sdk.types.AbstractAddress;
import com.concordium.sdk.types.UInt8;

import java.util.List;

/**
 * Wrapper class for {@link Transfer} enforcing the correct Token amount/id for cis2-wCCD contract.
 */
public class WCCDTransfer extends Transfer<TokenIdUnit, TokenAmountU64> {
    public WCCDTransfer(TokenIdUnit tokenId, TokenAmountU64 amount, AbstractAddress from, Receiver to, List<UInt8> data) {
        super(tokenId, amount, from, to, data);
    }
}
