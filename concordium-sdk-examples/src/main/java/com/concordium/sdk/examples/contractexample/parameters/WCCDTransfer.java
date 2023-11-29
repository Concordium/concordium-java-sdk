package com.concordium.sdk.examples.contractexample.parameters;

import com.concordium.sdk.types.AbstractAddress;

/**
 * Wrapper class for {@link Cis2Transfer} enforcing the correct Token amount/id for cis2-wCCD contract.
 */
public class WCCDTransfer extends Cis2Transfer<TokenIdUnit, TokenAmountU64> {
    public WCCDTransfer(TokenIdUnit tokenId, TokenAmountU64 amount, AbstractAddress from, Receiver to, byte[] data) {
        super(tokenId, amount, from, to, data);
    }
}
