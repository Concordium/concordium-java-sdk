package com.concordium.sdk.cis2;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.net.URL;
import java.util.Optional;

/**
 * A single response to a {@link Cis2Client#tokenMetadata(String...)} query.
 */
@ToString
@EqualsAndHashCode
public class TokenMetadata {

    /**
     * The metadata url.
     */
    @Getter
    private final URL metadataUrl;

    /**
     * An optional checksum of the metadata url.
     */
    private final byte[] checksum;


    public TokenMetadata(URL metadataUrl, byte[] checksum) {
        this.metadataUrl = metadataUrl;
        this.checksum = checksum;
    }

    /**
     * Get the checksum if available.
     * @return the checksum
     */
    public Optional<byte[]> getChecksum() {
        return Optional.ofNullable(this.checksum);
    }
}
