package com.concordium.sdk.transactions;

import com.concordium.sdk.exceptions.TransactionCreationException;

import java.util.Objects;

public interface Transaction {
    /**
     * Returns serialized {@link Transaction}
     * This is the raw bytes that is sent to the node.
     *
     * @return The serialized {@link Transaction}
     */
    byte[] getBytes();

    /**
     * Get the {@link Hash} of the {@link Transaction}
     *
     * @return the hash
     */
    Hash getHash();

    /**
     * The network id.
     * The default value is {@link Transaction#DEFAULT_NETWORK_ID}
     *
     * @return the network id.
     */
    int getNetworkId();

    int DEFAULT_NETWORK_ID = 100;

    static void verifyTransferInput(AccountAddress sender, AccountNonce nonce, Expiry expiry, AccountAddress receiver, CCDAmount amount, TransactionSigner signer) throws TransactionCreationException {
        if (Objects.isNull(sender)) {
            throw TransactionCreationException.from(new IllegalArgumentException("Sender cannot be null"));
        }
        if (Objects.isNull(nonce)) {
            throw TransactionCreationException.from(new IllegalArgumentException("AccountNonce cannot be null"));
        }
        if (Objects.isNull(expiry)) {
            throw TransactionCreationException.from(new IllegalArgumentException("Expiry cannot be null"));
        }

        if (Objects.isNull(receiver)) {
            throw TransactionCreationException.from(new IllegalArgumentException("Receiver cannot be null"));
        }
        if (Objects.isNull(amount)) {
            throw TransactionCreationException.from(new IllegalArgumentException("Amount cannot be null"));
        }
        if (Objects.isNull(signer) || signer.isEmpty()) {
            throw TransactionCreationException.from(new IllegalArgumentException("Signer cannot be null or empty"));
        }
    }

    static void verifyCommonInput(AccountAddress sender, AccountNonce nonce, Expiry expiry, TransactionSigner signer) throws TransactionCreationException {
        if (Objects.isNull(sender)) {
            throw TransactionCreationException.from(new IllegalArgumentException("Sender cannot be null"));
        }
        if (Objects.isNull(nonce)) {
            throw TransactionCreationException.from(new IllegalArgumentException("AccountNonce cannot be null"));
        }
        if (Objects.isNull(expiry)) {
            throw TransactionCreationException.from(new IllegalArgumentException("Expiry cannot be null"));
        }
        if (Objects.isNull(signer) || signer.isEmpty()) {
            throw TransactionCreationException.from(new IllegalArgumentException("Signer cannot be null or empty"));
        }

    }

}
