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
    private final AccountAddress to;

    private final Memo memo;

    private final UInt64 maxEnergyCost;

    public EncryptedTransferWithMemo(EncryptedAmountTransferData data, AccountAddress to, Memo memo, UInt64 maxEnergyCost) {
        this.data = data;
        this.to = to;
        this.memo = memo;
        this.maxEnergyCost = maxEnergyCost;
    }

    @Override
    public PayloadType getType() {
        return PayloadType.ENCRYPTED_TRANSFER_WITH_MEMO;
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
        return this.maxEnergyCost;
    }

    static EncryptedTransferWithMemo createNew(EncryptedAmountTransferData data, AccountAddress to, Memo memo, UInt64 maxEnergyCost) {
        return new EncryptedTransferWithMemo(data, to, memo, maxEnergyCost);
    }
}
