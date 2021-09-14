package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import lombok.val;

import java.util.*;

public class TransactionSigner {
    private final Map<Byte, Map<Byte, ED25519SecretKey>> keys = new HashMap<>();

    public TransactionSigner put(Byte credentialIndex, Byte keyIndex, ED25519SecretKey key) {
        if (Objects.isNull(keys.get(credentialIndex))) {
            keys.put(credentialIndex, new HashMap<>());
        }
        keys.get(credentialIndex).put(keyIndex, key);
        return this;
    }

    final TransactionSignature sign(byte[] message) {
        val transactionSignature = new TransactionSignature();
        for (Byte credentialIndex : keys.keySet()) {
            val keys = this.keys.get(credentialIndex);
            for (Byte keyIndex : keys.keySet()) {
                transactionSignature.put(credentialIndex, keyIndex, keys.get(keyIndex).sign(message));
            }
        }
        return transactionSignature;
    }

    public int size() {
        int size = 0;
        for (Byte credentialIndex : keys.keySet()) {
            size += keys.get(credentialIndex).size();
        }
        return size;
    }
}
