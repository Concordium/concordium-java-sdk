package com.concordium.sdk.transactions;

import lombok.val;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class TransactionSignerImpl implements TransactionSigner {

    private final Map<Index, Map<Index, Signer>> signers = new HashMap<>();

    /**
     * Use {@link TransactionSigner#from(SignerEntry...)} to instantiate a
     * TransactionSignerImpl
     */
    TransactionSignerImpl() {
    }


    @Override
    public TransactionSigner put(Index credentialIndex, Index keyIndex, Signer signer) {
        if (Objects.isNull(signers.get(credentialIndex))) {
            signers.put(credentialIndex, new HashMap<>());
        }
        signers.get(credentialIndex).put(keyIndex, signer);
        return this;
    }

    /**
     * @param message Input message byte array.
     * @return {@link TransactionSignature}.
     */
    @Override
    public TransactionSignature sign(byte[] message) {
        val transactionSignature = new TransactionSignature();
        for (Index credentialIndex : signers.keySet()) {
            val keys = this.signers.get(credentialIndex);
            for (Index index : keys.keySet()) {
                transactionSignature.put(credentialIndex, index, keys.get(index).sign(message));
            }
        }
        return transactionSignature;
    }

    @Override
    public int size() {
        int size = 0;
        for (Index credentialIndex : signers.keySet()) {
            size += signers.get(credentialIndex).size();
        }
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }
}
