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
public class Memo {

    public final static int BYTES = 256;

    @Getter
    private final byte[] value;

    private Memo(byte[] bytes) {
        this.value = bytes;
    }

    public static Memo from(String hexString) {
        try {
            return new Memo(Hex.decodeHex(hexString));
        } catch (DecoderException e) {
            throw new IllegalArgumentException("Not valid hex provided: ", e);
        }
    }

    public static Memo from(com.concordium.grpc.v2.Memo memo) {
        return Memo.from(memo.getValue().toByteArray());
    }

    // returns the serialized memo with suffixed length
    byte[] getBytes() {
        val buffer = ByteBuffer.allocate(UInt16.BYTES + value.length);
        buffer.put(UInt16.from(value.length).getBytes());
        buffer.put(this.value);
        return buffer.array();
    }

    public static Memo fromBytes(ByteBuffer source) {
        val len = UInt16.fromBytes(source);
        val bytes = new byte[len.getValue()];
        source.get(bytes);
        return new Memo(bytes);
    }

    // return the length of the serialized memo
    int getLength() {
        return UInt16.BYTES + value.length;
    }

    /**
     * Initializes {@link Memo} from byte array.
     *
     * @param memo Byte array
     * @return Initialized {@link Memo}
     * @throws TransactionCreationException If input byte array is NULL Or its length does not match {@link Memo#BYTES}
     */
    public static Memo from(byte[] memo) {
        if (Objects.isNull(memo)) {
            throw TransactionCreationException.from(new IllegalArgumentException("Memo cannot be null"));
        }
        if (memo.length > BYTES) {
            throw TransactionCreationException.from(
                    new IllegalArgumentException("Size of memo cannot exceed " + BYTES + " bytes"));
        }
        return new Memo(memo);
    }
}
