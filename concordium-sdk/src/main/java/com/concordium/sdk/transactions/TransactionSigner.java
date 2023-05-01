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
        if (json.has("value")) {json = json.get("value");}
        val credentialEntries = json.get("accountKeys").get("keys");
        int nrOfCredentials = credentialEntries.size();
        for (int i = 0; i < nrOfCredentials; i++) {
            val credentialIndex = Index.from(i);
            val entryKeys = credentialEntries.get(String.valueOf(i)).get("keys");
            int nrOfKeys = entryKeys.size();
            for (int j = 0; j < nrOfKeys; j++) {
                val keyIndex = Index.from(j);
                val signKey = entryKeys.get(String.valueOf(j)).get("signKey").asText();
                val signer = ED25519SecretKey.from(signKey);
                transactionSigner.put(credentialIndex, keyIndex, signer);
            }
        }
        return transactionSigner;
    }

}
