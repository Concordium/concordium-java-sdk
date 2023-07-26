package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.exceptions.ED25519Exception;
import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.val;

import java.io.File;
import java.io.IOException;

public interface TransactionSigner {

    TransactionSigner put(Index credentialIndex, Index keyIndex, Signer signer);

    /**
     * Signs the input `message`.
     *
     * @param message Input message byte array.
     * @return {@link TransactionSignature}
     * @throws ED25519Exception If the Signing fails.
     */
    TransactionSignature sign(byte[] message);

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

    /**
     * Creates a new {@link TransactionSignerImpl} from {@link File} containing exported wallet
     *
     * @param walletExport The {@link File} containing an exported wallet
     * @return the {@link TransactionSignerImpl} populated with keys from the exported wallet
     * @throws IOException if provided {@link File} does not contain exported wallet
     */
    static TransactionSignerImpl from(File walletExport) throws IOException {
        val transactionSigner = new TransactionSignerImpl();
        JsonNode json = JsonMapper.INSTANCE.readTree(walletExport);
        // Browser wallet format has field "value" otherwise structure for accessing keys is similar
        if (json.has("value")) {
            json = json.get("value");
        }
        val credentialsMap = json.get("accountKeys").get("keys"); // Map(index, Map(index, {signKey, verifyKey})
        val credentialsIterator = credentialsMap.fieldNames();
        while (credentialsIterator.hasNext()) {
            String credentialIndexString = credentialsIterator.next();
            val credentialIndex = Index.from(Integer.parseInt(credentialIndexString));
            val keysMap = credentialsMap.get(credentialIndexString).get("keys"); // Map(index, {signKey, verifyKey})
            val keysIterator = keysMap.fieldNames();
            while (keysIterator.hasNext()) {
                String keyIndexString = keysIterator.next();
                val keyIndex = Index.from(Integer.parseInt(keyIndexString));
                val signKey = keysMap.get(keyIndexString).get("signKey").asText();
                val signer = ED25519SecretKey.from(signKey);
                transactionSigner.put(credentialIndex, keyIndex, signer);
            }
        }
        return transactionSigner;
    }

}
