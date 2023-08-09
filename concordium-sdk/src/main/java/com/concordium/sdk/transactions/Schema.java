package com.concordium.sdk.transactions;

import lombok.Getter;

import java.io.*;

/**
 * TODO comment
 */
@Getter
public class Schema {

    private final byte[] schemaBytes;

    private final SchemaVersion version;

    Schema(byte[] schemaBytes, SchemaVersion version) {
        this.schemaBytes = schemaBytes;
        this.version = version;
    }

    public static Schema from(byte[] schemaBytes, SchemaVersion version) {
        return new Schema(schemaBytes, version);
    }

    public static Schema from(byte[] schemaBytes) {return new Schema(schemaBytes, SchemaVersion.UNKNOWN);}

    public static Schema from(InputStream stream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        return null;
    }


}
