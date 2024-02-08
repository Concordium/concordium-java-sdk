package com.concordium.sdk.cis2.events;

import com.concordium.sdk.cis2.TokenAmount;
import com.concordium.sdk.cis2.TokenId;
import com.concordium.sdk.types.AbstractAddress;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * An event that is emitted from a CIS2 compliant contract when a token has been transferred.
 * https://proposals.concordium.software/CIS/cis-2.html#transferevent
 */
@Getter
@EqualsAndHashCode
@ToString
public class TransferEvent implements Cis2Event {

    /**
     * The id of the token(s) that were transferred.
     */
    private final TokenId tokenId;

    /**
     * The amount of tokens that was transferred.
     */
    private final TokenAmount tokenAmount;

    /**
     * The previous owner of the token(s).
     */
    private final AbstractAddress from;

    /**
     * The new owner of the token(s).
     */
    private final AbstractAddress to;

    public TransferEvent(TokenId tokenId, TokenAmount tokenAmount, AbstractAddress from, AbstractAddress to) {
        this.tokenId = tokenId;
        this.tokenAmount = tokenAmount;
        this.from = from;
        this.to = to;
    }

    @Override
    public Type getType() {
        return Type.TRANSFER;
    }
}
