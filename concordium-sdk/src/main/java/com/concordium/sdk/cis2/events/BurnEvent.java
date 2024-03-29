package com.concordium.sdk.cis2.events;

import com.concordium.sdk.cis2.TokenAmount;
import com.concordium.sdk.cis2.TokenId;
import com.concordium.sdk.types.AbstractAddress;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * An event emitted by a CIS2 compliant smart contract when one or more tokens have been burned.
 * https://proposals.concordium.software/CIS/cis-2.html#burnevent
 */
@Getter
@ToString
@EqualsAndHashCode
public class BurnEvent implements Cis2Event {

    /**
     * The token that was burned.
     */
    private final TokenId tokenId;
    /**
     * The amount of tokens that was burned.
     */
    private final TokenAmount tokenAmount;
    /**
     * The owner of the token
     */
    private final AbstractAddress owner;

    public BurnEvent(TokenId tokenId, TokenAmount tokenAmount, AbstractAddress owner) {
        this.tokenId = tokenId;
        this.tokenAmount = tokenAmount;
        this.owner = owner;
    }

    @Override
    public Type getType() {
        return Type.BURN;
    }
}
