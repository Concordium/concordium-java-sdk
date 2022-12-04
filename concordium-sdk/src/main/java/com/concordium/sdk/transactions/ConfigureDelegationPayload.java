package com.concordium.sdk.transactions;

import com.concordium.sdk.responses.transactionstatus.DelegationTarget;
import com.concordium.sdk.types.UInt16;
import lombok.*;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

@Getter
@Builder
@EqualsAndHashCode
public class ConfigureDelegationPayload {
    /**
     * The capital delegated to the pool.
     */
    private final CCDAmount capital;
    /**
     * Whether the delegator's earnings are restaked.
     */
    private final Boolean restakeEarnings;
    /**
     * The target of the delegation.
     */
    private final DelegationTarget delegationTarget;

    ByteBuffer createNotNullBuffer(byte[] bufferBytes) {
        val buffer = ByteBuffer.allocate(bufferBytes.length);
        buffer.put(bufferBytes);
        return buffer;
    }

    public byte[] getBitMapBytes() {
        int bitValue = 0;
        int it = 1;
        try {
            for (Field f : getClass().getDeclaredFields()) {
                if (f.get(this) != null)
                    bitValue |= it;
                it *= 2;
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return UInt16.from(bitValue).getBytes();
    }

    public byte[] getBytes() {
        int bufferLength = 0;
        ByteBuffer capitalBuffer = ByteBuffer.allocate(bufferLength);
        ByteBuffer restakeEarningBuffer = ByteBuffer.allocate(bufferLength);
        ByteBuffer delegationTargetBuffer = ByteBuffer.allocate(bufferLength);

        byte[] bitMapBytes = getBitMapBytes();
        bufferLength += UInt16.BYTES;

        if (this.capital != null) {
            val capitalBufferBytes = this.capital.getBytes();
            capitalBuffer = createNotNullBuffer(capitalBufferBytes);
            bufferLength += capitalBufferBytes.length;
        }

        if (this.restakeEarnings != null) {
            val restakeEarningsByte = (byte)(this.restakeEarnings?1:0);
            restakeEarningBuffer = createNotNullBuffer(new byte[]{restakeEarningsByte});
            bufferLength += TransactionType.BYTES;
        }

        if (this.delegationTarget != null) {
            val openForDelegationBytes = this.delegationTarget.getBytes();
            delegationTargetBuffer = createNotNullBuffer(openForDelegationBytes);
            bufferLength += TransactionType.BYTES;
        }

        val buffer = ByteBuffer.allocate(bufferLength);

        buffer.put(bitMapBytes);

        buffer.put(capitalBuffer.array());
        buffer.put(restakeEarningBuffer.array());
        buffer.put(delegationTargetBuffer.array());

        return buffer.array();
    }
}
