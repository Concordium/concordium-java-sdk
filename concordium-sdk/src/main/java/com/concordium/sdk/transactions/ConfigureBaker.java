package com.concordium.sdk.transactions;

import com.concordium.sdk.responses.transactionstatus.OpenStatus;
import com.concordium.sdk.responses.transactionstatus.PartsPerHundredThousand;
import com.concordium.sdk.types.UInt16;
import com.concordium.sdk.types.UInt32;
import lombok.*;

import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * Payload for configuring a baker.
 * The [Default] implementation produces an empty configure that will have no
 * effects.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class ConfigureBaker extends Payload {
    /**
     * The equity capital of the baker.
     * I.e. the effective stake of the baker.
     */
    @Nullable
    private final CCDAmount capital;
    /**
     * Whether the baker's earnings are restaked.
     */
    @Nullable
    private final Boolean restakeEarnings;
    /**
     * Whether the pool is open for delegators.
     */
    @Nullable
    private final OpenStatus openForDelegation;
    /**
     * The key/proof pairs to verify the baker.
     */
    @Nullable
    private final ConfigureBakerKeysPayload keysWithProofs;
    /**
     * The URL referencing the baker's metadata.
     */
    @Nullable
    private final String metadataUrl;

    /**
     * The commission the pool owner takes on transaction fees.
     * The supplied value is interpreted as "the value" / 100_000
     */
    @Nullable
    private final PartsPerHundredThousand transactionFeeCommission;
    /**
     * The commission the pool owner takes on baking rewards.
     * The supplied value is interpreted as "the value" / 100_000
     */
    @Nullable
    private final PartsPerHundredThousand bakingRewardCommission;
    /**
     * The commission the pool owner takes on finalization rewards.
     * The supplied value is interpreted as "the value" / 100_000
     */
    @Nullable
    private final PartsPerHundredThousand finalizationRewardCommission;
    /**
     * Whether the validator is suspended.
     * This is only supported from protocol version 8 onwards
     */
    @Nullable
    private final Boolean suspended;

    @Builder
    public ConfigureBaker(@Nullable CCDAmount capital,
                          @Nullable Boolean restakeEarnings,
                          @Nullable OpenStatus openForDelegation,
                          @Nullable ConfigureBakerKeysPayload keysWithProofs,
                          @Nullable String metadataUrl,
                          @Nullable PartsPerHundredThousand transactionFeeCommission,
                          @Nullable PartsPerHundredThousand bakingRewardCommission,
                          @Nullable PartsPerHundredThousand finalizationRewardCommission,
                          @Nullable Boolean suspended) {
        super(TransactionType.CONFIGURE_BAKER);
        this.capital = capital;
        this.restakeEarnings = restakeEarnings;
        this.openForDelegation = openForDelegation;
        this.keysWithProofs = keysWithProofs;
        this.metadataUrl = metadataUrl;
        this.transactionFeeCommission = transactionFeeCommission;
        this.bakingRewardCommission = bakingRewardCommission;
        this.finalizationRewardCommission = finalizationRewardCommission;
        this.suspended = suspended;
    }

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
        bitValue |= (!(Objects.isNull(this.restakeEarnings)) ? it : 0);
        it *= 2;
        bitValue |= (!Objects.isNull(this.openForDelegation) ? it : 0);
        it *= 2;
        bitValue |= ((this.keysWithProofs != null) ? it : 0);
        it *= 2;
        bitValue |= ((this.metadataUrl != null) ? it : 0);
        it *= 2;
        bitValue |= (!Objects.isNull(this.transactionFeeCommission) ? it : 0);
        it *= 2;
        bitValue |= (!Objects.isNull(this.bakingRewardCommission) ? it : 0);
        it *= 2;
        bitValue |= (!Objects.isNull(this.finalizationRewardCommission) ? it : 0);
        it *= 2;
        bitValue |= (!Objects.isNull(this.suspended) ? it : 0);

        return UInt16.from(bitValue).getBytes();
    }

    @Override
    protected byte[] getPayloadBytes() {
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
        ByteBuffer suspendedBuffer = ByteBuffer.allocate(bufferLength);

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
            byte openForDelegationByte;
            switch (this.openForDelegation) {
                case OPEN_FOR_ALL:
                    openForDelegationByte = 0;
                    break;
                case CLOSED_FOR_NEW:
                    openForDelegationByte = 1;
                    break;
                case CLOSED_FOR_ALL:
                    openForDelegationByte = 2;
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized open status " + this.openForDelegation);
            }
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

        if (this.suspended != null) {
            val suspendedByte = (byte) (this.suspended ? 1 : 0);
            suspendedBuffer = createNotNullBuffer(new byte[]{suspendedByte});
            bufferLength += TransactionType.BYTES;
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
        buffer.put(suspendedBuffer.array());

        return buffer.array();
    }
}
