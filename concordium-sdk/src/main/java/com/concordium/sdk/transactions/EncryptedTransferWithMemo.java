package com.concordium.sdk.transactions;


import com.concordium.sdk.types.UInt64;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public final class EncryptedTransferWithMemo extends Payload {
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

    public EncryptedTransferWithMemo(EncryptedAmountTransferData data, AccountAddress receiver, Memo memo) {
        this.data = data;
        this.receiver = receiver;
        this.memo = memo;
    }

    @Override
    public PayloadType getType() {
        return PayloadType.ENCRYPTED_TRANSFER_WITH_MEMO;
    }

    @Override
    UInt64 getTransactionTypeCost() {
        return TransactionTypeCost.ENCRYPTED_TRANSFER.getValue();
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.ENCRYPTED_TRANSFER_WITH_MEMO;
    }

    @Override
    public byte[] getTransactionPayloadBytes() {
        val toAddress = receiver.getBytes();
        byte[] dataBytes = data.getBytes();
        val buffer = ByteBuffer.allocate(
                +toAddress.length
                        + dataBytes.length
                        + memo.getLength());
        buffer.put(toAddress);
        buffer.put(memo.getBytes());
        buffer.put(dataBytes);

        return buffer.array();
    }

    static EncryptedTransferWithMemo createNew(EncryptedAmountTransferData data, AccountAddress receiver, Memo memo) {
        return new EncryptedTransferWithMemo(data, receiver, memo);
    }
}
