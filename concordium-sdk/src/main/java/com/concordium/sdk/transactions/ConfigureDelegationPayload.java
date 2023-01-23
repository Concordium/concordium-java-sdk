package com.concordium.sdk.transactions;

import com.concordium.sdk.responses.transactionstatus.DelegationTarget;
import com.concordium.sdk.types.UInt16;
import lombok.*;

import java.nio.ByteBuffer;

/**
 * The ConfigureDelegationPayload class represents the payload of the ConfigureDelegation transaction.
 * It contains the capital delegated to the pool, whether the delegator's earnings are restaked, and the target of the delegation.
 * The class contains methods to convert the payload to bytes, and a bitmap representation of the non-null fields in the payload.
 */
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

    /**
     * This method returns an array of bytes that represents the bitmap of the fields that are not null
     * in the ConfigureDelegationPayload object
     * @return  byte[]
     */
    public byte[] getBitMapBytes() {
        int bitValue = 0;
        int it = 1;

        bitValue |= ((this.capital != null) ? it: 0);
        it *= 2;
        bitValue |= ((this.restakeEarnings != null) ? it: 0);
        it *= 2;
        bitValue |= ((this.delegationTarget != null) ? it: 0);

        return UInt16.from(bitValue).getBytes();
    }

    /**
     * This method returns an array of bytes that represents the ConfigureDelegationPayload object
     * @return byte[]
     */
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
