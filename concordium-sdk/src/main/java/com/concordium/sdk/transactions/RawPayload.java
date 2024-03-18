package com.concordium.sdk.transactions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;

@ToString
@EqualsAndHashCode(callSuper = false)
public class RawPayload extends Payload {

    /**
     * The bytes of the raw payload.
     */

    @Getter
    private final byte[] rawBytes;

    public RawPayload(final byte[] bytes) {
        this.rawBytes = bytes;
    }


    /**
     * Create a raw payload from the provided bytes
     * @param rawPayloadBytes the raw bytes of the payload
     * @throws IllegalArgumentException if the provided bytes does not contain a tag or if the tag cannot be parsed successfully.
     * @return a {@link RawPayload} where only the tag is parsed.
     */
    public static RawPayload from(byte[] rawPayloadBytes) {
        //try parse the tag
        if(rawPayloadBytes.length < 1) {
            throw new IllegalArgumentException("The provided bytes does not even contain a tag, so it cannot be a valid payload.");
        }
        TransactionType.parse(rawPayloadBytes[0]);
        return new RawPayload(rawPayloadBytes);
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.parse(this.rawBytes[0]);
    }

    @Override
    public byte[] getRawPayloadBytes() {
        return Arrays.copyOfRange(this.rawBytes, 1, this.rawBytes.length);
    }
}
