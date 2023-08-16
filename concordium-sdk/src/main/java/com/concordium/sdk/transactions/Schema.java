package com.concordium.sdk.transactions;

import lombok.Getter;

/**
 * The schema for a smart contract. Used when creating {@link SchemaParameter}.
 */
@Getter
public class Schema {

    /**
     * The bytes of the schema.
     */
    private final byte[] schemaBytes;

    /**
     * The version of the schema.
     */
    private final SchemaVersion version;

    /**
     * Creates a {@link Schema} from the provided bytes and {@link SchemaVersion}.
     * @param schemaBytes the bytes of the schema.
     * @param version the version of the schema.
     */
    Schema(byte[] schemaBytes, SchemaVersion version) {
        this.schemaBytes = schemaBytes;
        this.version = version;
    }

    /**
     * Creates a {@link Schema} from the provided bytes and {@link SchemaVersion}.
     * @param schemaBytes the bytes of the schema.
     * @param version the version of the schema.
     * @return {@link Schema} containing the provided bytes.
     */
    public static Schema from(byte[] schemaBytes, SchemaVersion version) {
        return new Schema(schemaBytes, version);
    }

    /**
     * Creates a schema from the provided bytes.
     * @param schemaBytes the bytes of the schema.
     * @return {@link Schema} containing the provided bytes.
     */
    public static Schema from(byte[] schemaBytes) {return new Schema(schemaBytes, SchemaVersion.UNKNOWN);}
}
