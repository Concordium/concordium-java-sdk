package com.concordium.sdk.cis2;

import com.concordium.sdk.types.AbstractAddress;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class BalanceQuery {
    private final String tokenId;
    private final AbstractAddress address;

    public BalanceQuery(String hexTokenId, AbstractAddress address) {
        this.tokenId = hexTokenId;
        this.address = address;
    }

}
