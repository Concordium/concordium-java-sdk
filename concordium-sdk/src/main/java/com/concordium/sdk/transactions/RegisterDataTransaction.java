package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
public class RegisterDataTransaction extends AbstractTransaction {
    private final Data data;

    @Builder
    public RegisterDataTransaction(
            final AccountAddress sender,
            final Data data,
            final AccountNonce nonce,
            final Expiry expiry,
            final TransactionSigner signer) {
        super(sender, nonce, expiry, signer);

        if (Objects.isNull(data)) {
            throw TransactionCreationException.from(new IllegalArgumentException("Data cannot be null"));
        }
        this.data = data;
    }

    @Override
    public BlockItem getBlockItem() {
        return RegisterData.createNew(getData())
                .withHeader(getHeader())
                .signWith(getSigner())
                .toBlockItem();
    }
}
