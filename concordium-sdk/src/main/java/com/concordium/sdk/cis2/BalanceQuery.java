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
    private final byte[] tokenId;
    private final AbstractAddress address;

    public BalanceQuery(byte[] hexTokenId, AbstractAddress address) {
        this.tokenId = hexTokenId;
        this.address = address;
    }

}
