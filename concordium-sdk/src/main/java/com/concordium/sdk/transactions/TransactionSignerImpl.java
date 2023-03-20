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
        val transactionSignature = TransactionSignature.builder();
        for (Index credentialIndex : signers.keySet()) {
            val accntSigs
                    = TransactionSignatureAccountSignatureMap.builder();
            val keys = this.signers.get(credentialIndex);
            for (Index index : keys.keySet()) {
                accntSigs.signature(index, Signature.from(keys.get(index).sign(message)));
            }
            transactionSignature.signature(credentialIndex, accntSigs.build());
        }

        return transactionSignature.build();
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
