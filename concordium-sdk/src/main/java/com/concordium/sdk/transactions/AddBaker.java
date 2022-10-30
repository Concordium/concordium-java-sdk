package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.bakertransactions.*;
import com.concordium.sdk.types.UInt64;
import lombok.*;

import java.nio.ByteBuffer;


/**
 * Payload of the `AddBaker` transaction. This transaction registers the
 * account as a baker.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@ToString
public final class AddBaker extends Payload {

    /**
     * The keys with which the baker registered.
     */
    private final AddBakerKeysPayload payload;
    /**
     * Initial baking stake.
     */
    private final CCDAmount bakingState;
    /**
     * Whether to add earnings to the stake automatically or not.
     */
    private final boolean restakeEarnings;

    private final UInt64 maxEnergyCost;

    @Override
    public PayloadType getType() {
        return PayloadType.ADD_BAKER;
    }

    @Override
    UInt64 getTransactionTypeCost() {
        return this.header.getMaxEnergyCost();
    }

    public byte[] getBytes() {
        val payloadBytes = payload.getBytes();
        val bufferLength = TransactionType.BYTES +
                CCDAmount.BYTES +
                payloadBytes.length +
                TransactionType.BYTES;

        val buffer = ByteBuffer.allocate(bufferLength);

        buffer.put(TransactionType.ADD_BAKER.getValue());
        buffer.put(payloadBytes);
        buffer.put(bakingState.getBytes());
        buffer.put((byte)(restakeEarnings?1:0));

        return buffer.array();
    }

    static AddBaker createNew(AccountAddress sender, CCDAmount bakingState, boolean restakeEarnings, UInt64 maxEnergyCost) {
        BakerKeysJniOutput bakerKeys = BakerKeys.createBakerKeys();
        AddBakerKeysJniInput input = AddBakerKeysJniInput.builder()
                .keys(bakerKeys)
                .sender(sender)
                .build();

        AddBakerKeysJniOutput output = AddBakerKeys.generateAddBakerKeysPayload(input);
        AddBakerKeysPayload addBakerPayload = AddBakerKeysPayload.from(output);

        return new AddBaker(
                addBakerPayload,
                bakingState,
                restakeEarnings,
                maxEnergyCost
        );
    }

}
