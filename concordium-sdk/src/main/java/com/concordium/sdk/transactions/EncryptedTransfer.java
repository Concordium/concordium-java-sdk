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
public final class EncryptedTransfer extends Payload {
    /**
     *  Data that will go onto an encrypted amount transfer.
     */
    private final EncryptedAmountTransferData data;

    /**
     * Account Address of the sender.
     */
    private final AccountAddress to;


    public EncryptedTransfer(EncryptedAmountTransferData data, AccountAddress to) {
        this.data = data;
        this.to = to;
    }

    @Override
    public PayloadType getType() {
        return PayloadType.ENCRYPTED_TRANSFER;
    }

    @Override
    byte[] getBytes() {
        val toAddress = to.getBytes();
        byte[] dataBytes = new byte[0];
        try {
            dataBytes = data.getBytes();
        } catch (DecoderException e) {
            throw new RuntimeException(e);
        }
        val buffer = ByteBuffer.allocate(
                TransactionType.BYTES
                        + toAddress.length
                        + dataBytes.length);
        buffer.put(TransactionType.ENCRYPTED_TRANSFER.getValue());
        buffer.put(toAddress);
        buffer.put(dataBytes);

        return buffer.array();

    }

    @Override
    UInt64 getTransactionTypeCost() {
        return UInt64.from(27000);
    }

    static EncryptedTransfer createNew(EncryptedAmountTransferData data, AccountAddress to) {
        return new EncryptedTransfer(data, to);
    }
}
