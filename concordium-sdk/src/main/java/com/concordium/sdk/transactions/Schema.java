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

    /**
     * TODO comment
     * taken from https://www.baeldung.com/convert-input-stream-to-array-of-bytes
     * @param is
     * @param version
     * @return
     * @throws IOException
     */
    public static Schema from(InputStream is, SchemaVersion version) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[4];

        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();
        byte[] targetArray = buffer.toByteArray();
        return from(targetArray, version);
    }


}
