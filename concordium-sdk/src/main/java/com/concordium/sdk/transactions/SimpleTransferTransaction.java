package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.ED25519Exception;
import com.concordium.sdk.exceptions.TransactionCreationException;
import lombok.Builder;
import lombok.Getter;
import lombok.val;

import java.util.Objects;

@Getter
public class SimpleTransferTransaction implements Transaction {
    private final AccountAddress sender;
    private final AccountAddress receiver;
    private final GTUAmount amount;
    private final AccountNonce nonce;
    private final Expiry expiry;
    private final TransactionSigner signer;

    private BlockItem item;

    @Builder
    public SimpleTransferTransaction(AccountAddress sender,
                                     AccountAddress receiver,
                                     GTUAmount amount,
                                     AccountNonce nonce,
                                     Expiry expiry,
                                     TransactionSigner signer) throws TransactionCreationException {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.nonce = nonce;
        this.expiry = expiry;
        this.signer = signer;
    }

    public static SimpleTransferTransactionBuilder builder() {
        return new CustomBuilder();
    }

    @Override
    public byte[] getBytes() {
        return item.getVersionedBytes();
    }

    @Override
    public Hash getHash() {
        return item.getHash();
    }

    @Override
    public int getNetworkId() {
        return DEFAULT_NETWORK_ID;
    }

    private static class CustomBuilder extends SimpleTransferTransaction.SimpleTransferTransactionBuilder {
        @Override
        public SimpleTransferTransaction build() throws TransactionCreationException {
            val transaction = super.build();
            assertNonNullParameters(transaction);
            transaction.item = createSimpleTransfer(transaction).toBlockItem();
            return transaction;
        }

        private Payload createSimpleTransfer(SimpleTransferTransaction transaction) throws TransactionCreationException {
            return SimpleTransfer.makeNew(
                            transaction.receiver,
                            transaction.amount.getValue()).
                    withHeader(TransactionHeader.builder()
                            .sender(transaction.sender)
                            .accountNonce(transaction.nonce.getNonce())
                            .expiry(transaction.expiry.getValue())
                            .build())
                    .signWith(transaction.signer);
        }

        private void assertNonNullParameters(SimpleTransferTransaction transaction) throws TransactionCreationException {
            if (Objects.isNull(transaction.sender)) {
                throw TransactionCreationException.from(new IllegalArgumentException("Sender cannot be null"));
            }
            if (Objects.isNull(transaction.receiver)) {
                throw TransactionCreationException.from(new IllegalArgumentException("Receiver cannot be null"));
            }
            if (Objects.isNull(transaction.amount)) {
                throw TransactionCreationException.from(new IllegalArgumentException("Amount cannot be null"));
            }
            if (Objects.isNull(transaction.nonce)) {
                throw TransactionCreationException.from(new IllegalArgumentException("AccountNonce cannot be null"));
            }
            if (Objects.isNull(transaction.expiry)) {
                throw TransactionCreationException.from(new IllegalArgumentException("Expiry cannot be null"));
            }
            if (Objects.isNull(transaction.signer) || transaction.signer.isEmpty()) {
                throw TransactionCreationException.from(new IllegalArgumentException("Signer cannot be null or empty"));
            }
        }

    }
}
