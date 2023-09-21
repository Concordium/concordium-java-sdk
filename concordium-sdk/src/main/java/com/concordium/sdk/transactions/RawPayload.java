package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt64;
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


    public static Payload from(byte[] rawPayloadBytes) {
        return new RawPayload(rawPayloadBytes);
    }

    /**
     * Returns null as the type cost is unknown for
     * a raw payload.
     */
    @Override
    protected UInt64 getTransactionTypeCost() {
        return null;
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
