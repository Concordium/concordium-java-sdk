package com.concordium.sdk.cis2;

import com.concordium.sdk.types.AbstractAddress;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * An object used for querying token balances.
 * See <a href="https://proposals.concordium.software/CIS/cis-2.html#balanceof">here</a> for the specification.
 */
@Getter
@ToString
@EqualsAndHashCode
public class BalanceQuery {

    /**
     * The token id to query
     */
    private final TokenId tokenId;

    /**
     * The address to query the balance of
     */
    private final AbstractAddress address;

    public BalanceQuery(TokenId tokenId, AbstractAddress address) {
        this.tokenId = tokenId;
        this.address = address;
    }

}
