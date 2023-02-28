package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
public class EncryptedTransferWithMemoTransaction extends AbstractTransaction {

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

    /**
     * A constructor of {@link EncryptedTransferWithMemoTransaction} class.
     */
    @Builder
    public EncryptedTransferWithMemoTransaction(
            final EncryptedAmountTransferData data,
            final AccountAddress receiver,
            final Memo memo,
            final AccountAddress sender,
            final AccountNonce nonce,
            final Expiry expiry,
            final TransactionSigner signer) {
        super(sender, nonce, expiry, signer);

        if (Objects.isNull(receiver)) {
            throw TransactionCreationException.from(new IllegalArgumentException("Receiver address cannot be null"));
        }
        if (Objects.isNull(memo)) {
            throw TransactionCreationException.from(new IllegalArgumentException("Memo cannot be null"));
        }
        if (Objects.isNull(data)) {
            throw TransactionCreationException.from(new IllegalArgumentException("Data cannot be null"));
        }
        this.data = data;
        this.receiver = receiver;
        this.memo = memo;
    }

    @Override
    public BlockItem getBlockItem() {
        return EncryptedTransferWithMemo.createNew(getData(), getReceiver(), getMemo()).
                withHeader(TransactionHeader.builder()
                        .sender(getSender())
                        .accountNonce(getNonce().getNonce())
                        .expiry(getExpiry().getValue())
                        .build())
                .signWith(getSigner())
                .toBlockItem();
    }
}
