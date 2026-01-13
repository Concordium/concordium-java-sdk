package com.concordium.sdk.transactions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;

@ToString
@EqualsAndHashCode(callSuper = true)
public class RawPayload extends Payload {

    /**
     * The bytes of the raw payload.
     */
    @Getter
    private final byte[] rawBytes;

    public RawPayload(final byte[] bytes) {
        super(TransactionType.parse(bytes[0]));
        this.rawBytes = bytes;
    }

    /**
     * Create a raw payload from the provided bytes
     *
     * @param rawPayloadBytes the raw bytes of the payload
     * @return a {@link RawPayload} where only the tag is parsed.
     * @throws IllegalArgumentException if the provided bytes does not contain a tag or if the tag cannot be parsed successfully.
     */
    public static RawPayload from(byte[] rawPayloadBytes) {
        //try parse the tag
        if (rawPayloadBytes.length < 1) {
            throw new IllegalArgumentException("The provided bytes does not even contain a tag, so it cannot be a valid payload.");
        }
        TransactionType.parse(rawPayloadBytes[0]);
        return new RawPayload(rawPayloadBytes);
    }

    @Override
    protected byte[] getPayloadBytes() {
        return Arrays.copyOfRange(this.rawBytes, 1, this.rawBytes.length);
    }
}
