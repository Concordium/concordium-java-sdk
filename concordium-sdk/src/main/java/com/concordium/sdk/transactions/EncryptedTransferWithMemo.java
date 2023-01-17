package com.concordium.sdk.transactions;


import com.concordium.sdk.types.UInt64;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.val;
import org.apache.commons.codec.DecoderException;

import java.nio.ByteBuffer;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public final class EncryptedTransferWithMemo extends Payload {
    /**
     *  Data that will go onto an encrypted amount transfer.
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
    byte[] getBytes() {
        val toAddress = receiver.getBytes();
        byte[] dataBytes = new byte[0];
        try {
            dataBytes = data.getBytes();
        } catch (DecoderException e) {
            throw new RuntimeException(e);
        }
        val buffer = ByteBuffer.allocate(
                TransactionType.BYTES
                        + toAddress.length
                        + dataBytes.length
                        + memo.getLength());
        buffer.put(TransactionType.ENCRYPTED_TRANSFER_WITH_MEMO.getValue());
        buffer.put(toAddress);
        buffer.put(memo.getBytes());
        buffer.put(dataBytes);

        return buffer.array();

    }

    @Override
    UInt64 getTransactionTypeCost() {
        return TransactionTypeCost.CONFIGURE_DELEGATION.getValue();
    }

    static EncryptedTransferWithMemo createNew(EncryptedAmountTransferData data, AccountAddress receiver, Memo memo) {
        return new EncryptedTransferWithMemo(data, receiver, memo);
    }
}
