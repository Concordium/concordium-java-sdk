package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt16;
import com.concordium.sdk.types.UInt32;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.val;

import java.nio.ByteBuffer;

/**
 * Payload for configuring a baker.
 * The [Default] implementation produces an empty configure that will have no
 * effects.
 */
@Getter
@Builder
@EqualsAndHashCode
public class ConfigureBakerPayload {
    /**
     * The equity capital of the baker.
     */
    private final CCDAmount capital;
    /**
     * Whether the baker's earnings are restaked.
     */
    private final Boolean restakeEarnings;
    /**
     * Whether the pool is open for delegators.
     */
    private final Integer openForDelegation;
    /**
     * The key/proof pairs to verify the baker.
     */
    private final ConfigureBakerKeysPayload keysWithProofs;
    /**
     * The URL referencing the baker's metadata.
     */
    private final String metadataUrl;
    /**
     * The commission the pool owner takes on transaction fees.
     */
    private final UInt32 transactionFeeCommission;
    /**
     * The commission the pool owner takes on baking rewards.
     */
    private final UInt32 bakingRewardCommission;
    /**
     * The commission the pool owner takes on finalization rewards.
     */
    private final UInt32 finalizationRewardCommission;

    ByteBuffer createNotNullBuffer(byte[] bufferBytes) {
        val buffer = ByteBuffer.allocate(bufferBytes.length);
        buffer.put(bufferBytes);
        return buffer;
    }

    public byte[] getBitMapBytes() {
        int bitValue = 0;
        int it = 1;

        bitValue |= ((this.capital != null) ? it : 0);
        it *= 2;
        bitValue |= ((this.restakeEarnings != null) ? it : 0);
        it *= 2;
        bitValue |= ((this.openForDelegation != null) ? it : 0);
        it *= 2;
        bitValue |= ((this.keysWithProofs != null) ? it : 0);
        it *= 2;
        bitValue |= ((this.metadataUrl != null) ? it : 0);
        it *= 2;
        bitValue |= ((this.transactionFeeCommission != null) ? it : 0);
        it *= 2;
        bitValue |= ((this.bakingRewardCommission != null) ? it : 0);
        it *= 2;
        bitValue |= ((this.finalizationRewardCommission != null) ? it : 0);

        return UInt16.from(bitValue).getBytes();
    }

    public byte[] getBytes() {
        int bufferLength = 0;
        ByteBuffer capitalBuffer = ByteBuffer.allocate(bufferLength);
        ByteBuffer restakeEarningBuffer = ByteBuffer.allocate(bufferLength);
        ByteBuffer openForDelegationBuffer = ByteBuffer.allocate(bufferLength);
        ByteBuffer keysWithProofsBuffer = ByteBuffer.allocate(bufferLength);
        ByteBuffer metadataUrlLengthBuffer = ByteBuffer.allocate(bufferLength);
        ByteBuffer metadataUrlBuffer = ByteBuffer.allocate(bufferLength);
        ByteBuffer transactionFeeCommissionBuffer = ByteBuffer.allocate(bufferLength);
        ByteBuffer bakingRewardCommissionBuffer = ByteBuffer.allocate(bufferLength);
        ByteBuffer finalizationRewardCommissionBuffer = ByteBuffer.allocate(bufferLength);

        byte[] bitMapBytes = getBitMapBytes();
        bufferLength += UInt16.BYTES;

        if (this.capital != null) {
            val capitalBufferBytes = this.capital.getBytes();
            capitalBuffer = createNotNullBuffer(capitalBufferBytes);
            bufferLength += capitalBufferBytes.length;
        }

        if (this.restakeEarnings != null) {
            val restakeEarningsByte = (byte) (this.restakeEarnings ? 1 : 0);
            restakeEarningBuffer = createNotNullBuffer(new byte[]{restakeEarningsByte});
            bufferLength += TransactionType.BYTES;
        }

        if (this.openForDelegation != null) {
            val openForDelegationByte = this.openForDelegation.byteValue();
            openForDelegationBuffer = createNotNullBuffer(new byte[]{openForDelegationByte});
            bufferLength += TransactionType.BYTES;
        }

        if (this.keysWithProofs != null) {
            val keysWithProofsBytes = this.keysWithProofs.getBytes();
            keysWithProofsBuffer = createNotNullBuffer(keysWithProofsBytes);
            bufferLength += keysWithProofsBytes.length;
        }

        if (this.metadataUrl != null) {
            val metadataUrlLengthBytes = UInt16.from(this.metadataUrl.length());
            metadataUrlLengthBuffer = createNotNullBuffer(metadataUrlLengthBytes.getBytes());
            val metadataUrlBytes = this.metadataUrl.getBytes();
            metadataUrlBuffer = createNotNullBuffer(metadataUrlBytes);
            bufferLength += UInt16.BYTES + metadataUrlBytes.length;
        }

        if (this.transactionFeeCommission != null) {
            val transactionFeeCommissionBytes = this.transactionFeeCommission.getBytes();
            transactionFeeCommissionBuffer = createNotNullBuffer(transactionFeeCommissionBytes);
            bufferLength += UInt32.BYTES;
        }

        if (this.bakingRewardCommission != null) {
            val bakingRewardCommissionBytes = this.bakingRewardCommission.getBytes();
            bakingRewardCommissionBuffer = createNotNullBuffer(bakingRewardCommissionBytes);
            bufferLength += UInt32.BYTES;
        }

        if (this.finalizationRewardCommission != null) {
            val finalizationRewardCommissionBytes = this.finalizationRewardCommission.getBytes();
            finalizationRewardCommissionBuffer = createNotNullBuffer(finalizationRewardCommissionBytes);
            bufferLength += UInt32.BYTES;
        }

        val buffer = ByteBuffer.allocate(bufferLength);

        buffer.put(bitMapBytes);

        buffer.put(capitalBuffer.array());
        buffer.put(restakeEarningBuffer.array());
        buffer.put(openForDelegationBuffer.array());
        buffer.put(keysWithProofsBuffer.array());
        buffer.put(metadataUrlLengthBuffer.array());
        buffer.put(metadataUrlBuffer.array());
        buffer.put(transactionFeeCommissionBuffer.array());
        buffer.put(bakingRewardCommissionBuffer.array());
        buffer.put(finalizationRewardCommissionBuffer.array());

        return buffer.array();
    }
}
