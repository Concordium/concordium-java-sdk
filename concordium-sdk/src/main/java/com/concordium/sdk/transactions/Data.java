package com.concordium.sdk.transactions;

import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.UInt16;
import lombok.*;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * Repesents Data in {@link com.concordium.sdk.transactions.RegisterData} Account Transaction
 */
@EqualsAndHashCode
@ToString
public class Data {

    /**
     * Maximum Byte length of {@link Data}
     */
    public final static int BYTES = 256;

    /**
     * The Data in Bytes Array
     */
    @Getter
    private final byte[] value;

    private Data(final byte @NonNull [] bytes) {
        this.value = bytes;
    }

    /**
     * Initializes {@link Data} from Hex encoded string
     *
     * @param hexString Hex String
     * @return Initialized {@link Data}
     */
    public static Data from(String hexString) {
        try {
            return new Data(Hex.decodeHex(hexString));
        } catch (DecoderException e) {
            throw new IllegalArgumentException("Not valid hex provided: ", e);
        }
    }

    /**
     * Returns the serialized data with suffixed length
     *
     * @return Raw bytes array
     */
    public byte[] getBytes() {
        val buffer = ByteBuffer.allocate(UInt16.BYTES + value.length);
        buffer.put(UInt16.from(value.length).getBytes());
        buffer.put(this.value);
        return buffer.array();
    }

    /**
     * Initializes {@link Data} from {@link java.nio.ByteBuffer}
     *
     * @param source The {@link ByteBuffer} to initialize from
     * @return Initialized {@link Data}
     */
    public static Data fromBytes(ByteBuffer source) {
        val len = UInt16.fromBytes(source);
        val bytes = new byte[len.getValue()];
        source.get(bytes);
        return new Data(bytes);
    }

    /**
     * Return the length of the serialized data
     *
     * @return Length of Serialized Byte Array
     */
    int getLength() {
        return UInt16.BYTES + value.length;
    }

    /**
     * Initializes {@link Data} from Byte Array
     *
     * @param data Data in Byte Array format
     * @return Initialized {@link Data}
     * @throws TransactionCreationException
     */
    public static Data from(byte[] data) throws TransactionCreationException {
        if (Objects.isNull(data)) {
            throw TransactionCreationException.from(new IllegalArgumentException("Data cannot be null"));
        }

        if (data.length > BYTES) {
            throw TransactionCreationException.from(
                    new IllegalArgumentException("Size of Data cannot exceed " + BYTES + " bytes"));
        }
        
        return new Data(data);
    }
}
