package com.concordium.sdk.transactions;

import com.concordium.sdk.types.AccountAddress;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public final class EncryptedTransfer extends Payload {
    /**
     * Data that will go onto an encrypted amount transfer.
     */
    private final EncryptedAmountTransferData data;

    /**
     * Account Address of the sender.
     */
    private final AccountAddress receiver;


    public EncryptedTransfer(EncryptedAmountTransferData data, AccountAddress receiver) {
        this.data = data;
        this.receiver = receiver;
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.ENCRYPTED_TRANSFER;
    }

    @Override
    protected byte[] getRawPayloadBytes() {
        val toAddress = receiver.getBytes();
        byte[] dataBytes = data.getBytes();
        val buffer = ByteBuffer.allocate(toAddress.length + dataBytes.length);
        buffer.put(toAddress);
        buffer.put(dataBytes);

        return buffer.array();
    }

    static EncryptedTransfer createNew(EncryptedAmountTransferData data, AccountAddress receiver) {
        return new EncryptedTransfer(data, receiver);
    }
}
