package com.concordium.sdk.transactions;

import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.UInt16;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.val;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.nio.ByteBuffer;
import java.util.Objects;

@EqualsAndHashCode
@ToString
public class Data {

    public final static int BYTES = 256;

    @Getter
    private final byte[] value;

    private Data(byte[] bytes) {
        this.value = bytes;
    }

    public static Data from(String hexString) {
        try {
            return new Data(Hex.decodeHex(hexString));
        } catch (DecoderException e) {
            throw new IllegalArgumentException("Not valid hex provided: ", e);
        }
    }

    // returns the serialized data with suffixed length
    byte[] getBytes() {
        val buffer = ByteBuffer.allocate(UInt16.BYTES + value.length);
        buffer.put(UInt16.from(value.length).getBytes());
        buffer.put(this.value);
        return buffer.array();
    }

    public static Data fromBytes(ByteBuffer source) {
        val len = UInt16.fromBytes(source);
        val bytes = new byte[len.getValue()];
        source.get(bytes);
        return new Data(bytes);
    }

    // return the length of the serialized data
    int getLength() {
        return UInt16.BYTES + value.length;
    }

    static Data from(byte[] data) throws TransactionCreationException {
        if (Objects.isNull(data)) {
            throw TransactionCreationException.from(new IllegalArgumentException("Data cannot be null"));
        }
        if (data.length > BYTES) {
            throw TransactionCreationException.from(
                    new IllegalArgumentException("Size of data cannot exceed " + BYTES + " bytes"));
        }
        return new Data(data);
    }
}
