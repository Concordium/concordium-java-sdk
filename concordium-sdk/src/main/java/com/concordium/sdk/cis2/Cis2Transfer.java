package com.concordium.sdk.cis2;

import com.concordium.sdk.types.AbstractAddress;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Object representing a CIS2 transfer https://proposals.concordium.software/CIS/cis-2.html#parameter
 *
 * Note that the maximum allowed size of the serialized parameter is 64kb.
 */
@ToString
@Getter
@EqualsAndHashCode
public class Cis2Transfer {
    /**
     * The hex encoded token id.
     */
    private final String hexEncodedTokenId;

    /**
     * The token amount.
     */
    private final int tokenAmount;

    /**
     * Sender of the token.
     */
    private final AbstractAddress sender;

    /**
     * Receiver of the token.
     */
    private final AbstractAddress receiver;

    /**
     * Additional data.
     */
    private final byte[] additionalData;

    public Cis2Transfer(String hexEncodedTokenId, int tokenAmount, AbstractAddress sender, AbstractAddress receiver, byte[] additionalData) {
        this.hexEncodedTokenId = hexEncodedTokenId;
        this.tokenAmount = tokenAmount;
        this.sender = sender;
        this.receiver = receiver;
        this.additionalData = additionalData;
    }
}
