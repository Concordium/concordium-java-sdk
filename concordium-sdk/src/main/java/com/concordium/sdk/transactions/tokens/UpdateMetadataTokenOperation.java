package com.concordium.sdk.transactions.tokens;

import com.concordium.sdk.transactions.Hash;
import com.concordium.sdk.types.UInt64;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.Optional;

/**
 * Update the token metadata of a token.
 * Supported from protocol version 11.
 */
@Getter
@Builder
@EqualsAndHashCode
@ToString
// This is for CBOR serialization to use the generated builder.
@Jacksonized
public class UpdateMetadataTokenOperation implements TokenOperation {

    /**
     * Metadata JSON URL.
     */
    @NonNull
    private final String url;

    /**
     * An optional 32 byte SHA-256 checksum of the metadata JSON.
     */
    private final Hash checksumSha256;

    public Optional<Hash> getChecksumSha256() {
        return Optional.ofNullable(checksumSha256);
    }

    @Override
    public UInt64 getBaseCost() {
        // TODO set the real value.
        return UInt64.from(300);
    }

    @Override
    public String getType() {
        return TYPE;
    }

    public static final String TYPE = "updateMetadata";
}
