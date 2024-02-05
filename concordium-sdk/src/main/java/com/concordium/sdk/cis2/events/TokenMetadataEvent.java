package com.concordium.sdk.cis2.events;

import com.concordium.sdk.cis2.TokenMetadata;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * An event that is emitted by a CIS2 compliant contract when a
 * particular token has its metadata url set.
 */
@Getter
@EqualsAndHashCode
@ToString
public class TokenMetadataEvent implements Cis2Event {

    /**
     * The token id which as
     */
    private final String hexTokenId;

    /**
     * The new metadata url for the token.
     */
    private final TokenMetadata metadataUrl;

    public TokenMetadataEvent(String hexTokenId, TokenMetadata metadataUrl) {
        this.hexTokenId = hexTokenId;
        this.metadataUrl = metadataUrl;
    }


    @Override
    public Type getType() {
        return Type.TOKEN_METADATA;
    }
}