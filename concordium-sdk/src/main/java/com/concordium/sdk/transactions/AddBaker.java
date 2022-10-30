package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.bakertransactions.BakerKeys;
import com.concordium.sdk.crypto.bakertransactions.BakerKeysJniOutput;
import lombok.Getter;
import lombok.ToString;
import lombok.val;
import org.apache.commons.codec.binary.Hex;

import java.nio.ByteBuffer;


/**
 * Payload of the `AddBaker` transaction. This transaction registers the
 * account as a baker.
 */
@ToString
@Getter
public final class AddBakerPayload {

    /**
     * The keys with which the baker registered.
     */
    private final AddBakerKeysPayload keys;
    /**
     * Initial baking stake.
     */
    private final CCDAmount bakingState;
    /**
     * Whether to add earnings to the stake automatically or not.
     */
    private final boolean restakeEarnings;

    private AddBakerPayload(AddBakerKeysPayload keys, CCDAmount bakingState, boolean restakeEarnings) {
        this.keys = keys;
        this.bakingState = bakingState;
        this.restakeEarnings = restakeEarnings;
    }

    public static AddBakerPayload from(int amount, boolean restakeEarnings) {
        BakerKeysJniOutput output = BakerKeys.createBakerKeys();
        AddBakerKeysPayload keys = AddBakerKeysPayload.from(String.valueOf(output));

        return new AddBakerPayload(
                keys,
                CCDAmount.fromMicro(amount),
                restakeEarnings
        );
    }

    public byte[] getBytes() {
        val amountBytes = bakingState.getBytes();
        val keysBytes = keys.getBytes();
        val bufferLength = TransactionType.BYTES +
                CCDAmount.BYTES +
                keysBytes.length +
                TransactionType.BYTES;

        val buffer = ByteBuffer.allocate(bufferLength);

        buffer.put(TransactionType.ADD_BAKER.getValue());
        buffer.put(keysBytes);
        buffer.put(amountBytes);
        buffer.put((byte)(restakeEarnings?1:0));

        return buffer.array();
    }
}
