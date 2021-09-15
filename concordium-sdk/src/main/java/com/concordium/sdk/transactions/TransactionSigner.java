package com.concordium.sdk.transactions;

import com.concordium.sdk.exceptions.ED25519Exception;
import lombok.val;

public interface TransactionSigner {

    TransactionSigner put(Index credentialIndex, Index keyIndex, Signer signer);

    TransactionSignature sign(byte[] message) throws ED25519Exception;

    int size();

    boolean isEmpty();

    /**
     * Creates a new {@link TransactionSignerImpl} given the provided
     * {@link SignerEntry}s
     *
     * @param entries The SignerEntries which should be used for signing the transaction.
     * @return the {@link TransactionSignerImpl} with the populated {@link SignerEntry}
     */
    static TransactionSignerImpl from(SignerEntry... entries) {
        val transactionSigner = new TransactionSignerImpl();
        for (SignerEntry entry : entries) {
            transactionSigner.put(entry.getCredentialIndex(), entry.getKeyIndex(), entry.getSigner());
        }
        return transactionSigner;
    }

}
