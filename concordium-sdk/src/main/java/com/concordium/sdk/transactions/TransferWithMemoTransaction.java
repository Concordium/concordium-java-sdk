package com.concordium.sdk.transactions;

import com.concordium.sdk.exceptions.TransactionCreationException;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
public class TransferWithMemoTransaction extends AbstractTransaction {
    private final AccountAddress receiver;
    private final CCDAmount amount;
    private final Memo memo;

    @Builder
    public TransferWithMemoTransaction(
            final AccountAddress sender,
            final AccountAddress receiver,
            final CCDAmount amount,
            final Memo memo,
            final AccountNonce nonce,
            final Expiry expiry,
            final TransactionSigner signer) {
        super(sender, nonce, expiry, signer);

        if (Objects.isNull(receiver)) {
            throw TransactionCreationException.from(new IllegalArgumentException("Receiver cannot be null"));
        }
        if (Objects.isNull(amount)) {
            throw TransactionCreationException.from(new IllegalArgumentException("Amount cannot be null"));
        }
        if (Objects.isNull(memo)) {
            throw TransactionCreationException.from(new IllegalArgumentException("Memo cannot be null"));
        }
        this.receiver = receiver;
        this.amount = amount;
        this.memo = memo;
    }

    @Override
    BlockItem getBlockItem() {
        return TransferWithMemo.createNew(getReceiver(), getAmount(), getMemo())
                .withHeader(getHeader())
                .signWith(getSigner())
                .toBlockItem();
    }
}

