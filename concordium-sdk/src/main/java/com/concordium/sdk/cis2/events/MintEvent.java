package com.concordium.sdk.cis2.events;

import com.concordium.sdk.types.AbstractAddress;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * An event emitted by a CIS2 compliant smart contract when one or more tokens have been minted.
 * https://proposals.concordium.software/CIS/cis-2.html#mintevent
 */
@Getter
@ToString
@EqualsAndHashCode
public class MintEvent implements Cis2Event {

    /**
     * ID of the token that was minted.
     */
    private final String hexTokenId;

    /**
     * Amount of tokens that was minted.
     */
    private final long tokenAmount;

    /**
     * The owner of the minted tokens.
     */
    private final AbstractAddress owner;

    public MintEvent(String hexTokenId, long tokenAmount, AbstractAddress owner) {
        this.hexTokenId = hexTokenId;
        this.tokenAmount = tokenAmount;
        this.owner = owner;
    }

    @Override
    public Type getType() {
        return Type.MINT;
    }
}
