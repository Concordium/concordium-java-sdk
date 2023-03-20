package com.concordium.sdk.transactions;

import lombok.Getter;
import lombok.val;

import java.nio.ByteBuffer;

@Getter
public class EncryptedTransferWithMemoPayload {
    /**
     * Data that will go onto an encrypted amount transfer.
     */
    private final EncryptedAmountTransferData data;

    /**
     * Account Address of the sender.
     */
    private final AccountAddress receiver;

    /**
     * The memo message associated with the transfer.
     */
    private final Memo memo;

    private EncryptedTransferWithMemoPayload(final EncryptedAmountTransferData data,
                                             final AccountAddress receiver,
                                             final Memo memo) {
        this.data = data;
        this.receiver = receiver;
        this.memo = memo;
    }

    public static EncryptedTransferWithMemoPayload from(final EncryptedAmountTransferData data,
                                                        final AccountAddress receiver,
                                                        final Memo memo) {
        return new EncryptedTransferWithMemoPayload(data, receiver, memo);
    }

    public byte[] getBytes() {
        val toAddress = receiver.getBytes();
        byte[] dataBytes = data.getBytes();
        val buffer = ByteBuffer.allocate(toAddress.length + dataBytes.length + memo.getLength());
        buffer.put(toAddress);
        buffer.put(memo.getBytes());
        buffer.put(dataBytes);

        return buffer.array();
    }
}
