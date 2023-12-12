package com.concordium.sdk.examples.contractexample.parameters;

import com.concordium.sdk.types.AbstractAddress;

/**
 * Wrapper class for {@link BalanceOfQuery} enforcing the correct Token id for cis2-wCCD contract.
 */
public class WCCDBalanceOfQuery extends BalanceOfQuery<TokenIdUnit> {
    public WCCDBalanceOfQuery(AbstractAddress address) {
        super(new TokenIdUnit(), address);
    }
}
