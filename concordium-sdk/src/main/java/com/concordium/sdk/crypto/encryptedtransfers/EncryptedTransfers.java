package com.concordium.sdk.crypto.encryptedtransfers;

import com.concordium.sdk.crypto.CryptoJniResultCode;
import com.concordium.sdk.crypto.NativeResolver;
import com.concordium.sdk.crypto.elgamal.ElgamalPublicKey;
import com.concordium.sdk.crypto.elgamal.ElgamalSecretKey;
import com.concordium.sdk.exceptions.CryptoJniException;
import com.concordium.sdk.responses.accountinfo.AccountEncryptedAmount;
import com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters;
import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.val;


public final class EncryptedTransfers {
    static {
        loadNatives();
    }

    static void loadNatives() {
        NativeResolver.loadLib();
    }

    /**
     * It takes a secret key, an encrypted amount, and an amount, and returns a payload that can be used to transfer the
     * amount from the encrypted amount to a public account
     *
     * @param cryptographicParameters global context with parameters for generating proofs, and generators for encryted amounts.
     * @param accountEncryptedAmount The encrypted amount of the account that is being transferred from.
     * @param secretKey The secret key of the sender (who is also the receiver).
     * @param amount The amount of money to transfer.
     * @return A payload of a secret to public amount transaction..
     */
    public static TransferToPublicJniOutput createSecToPubTransferPayload(
            CryptographicParameters cryptographicParameters,
            AccountEncryptedAmount accountEncryptedAmount,
            ElgamalSecretKey secretKey,
            CCDAmount amount) {
        return createSecToPubTransferPayload(TransferToPublicJniInput.builder()
                .global(GlobalContext.from(cryptographicParameters))
                .senderSecretKey(secretKey)
                .amount(amount)
                .inputEncryptedAmount(IndexedEncryptedAmount.from(accountEncryptedAmount))
                .build());
    }

    static TransferToPublicJniOutput createSecToPubTransferPayload(TransferToPublicJniInput jniInput) {

        TransferToPublicJniResult result = null;
        try {
            val inputJsonString = JsonMapper.INSTANCE.writeValueAsString(jniInput);
            val jsonStr = createSecToPubTransfer(inputJsonString);
            result = JsonMapper.INSTANCE.readValue(jsonStr, TransferToPublicJniResult.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (!result.isok()) {
            throw CryptoJniException.from(
                    result.getErr().orElse(CryptoJniResultCode.ERROR_UNKNOWN_RESULT_CODE));
        }

        return result.getOk().orElseThrow(
                () -> CryptoJniException.from(CryptoJniResultCode.ERROR_UNKNOWN_RESULT_CODE));
    }


    public static EncryptedAmountTransferJniOutput createEncryptedTransferPayload(
            CryptographicParameters cryptographicParameters,
            AccountEncryptedAmount inputAmount,
            ElgamalPublicKey receiverPublicKey,
            ElgamalSecretKey senderSecretKey,
            CCDAmount amountToSend) {
        return createEncryptedTransferPayload(EncryptedAmountTransferJniInput.builder()
                .global(GlobalContext.from(cryptographicParameters))
                .receiverPublicKey(receiverPublicKey)
                .senderSecretKey(senderSecretKey)
                .amountToSend(amountToSend)
                .inputEncryptedAmount(IndexedEncryptedAmount.from(inputAmount))
                .build());
    }

    static EncryptedAmountTransferJniOutput createEncryptedTransferPayload(EncryptedAmountTransferJniInput jniInput) {

        EncryptedAmountTransferJniResult result = null;
        try {
            val inputJsonString = JsonMapper.INSTANCE.writeValueAsString(jniInput);
            val jsonStr = generateEncryptedTransfer(inputJsonString);
            result = JsonMapper.INSTANCE.readValue(jsonStr, EncryptedAmountTransferJniResult.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (!result.isok()) {
            throw CryptoJniException.from(
                    result.getErr().orElse(CryptoJniResultCode.ERROR_UNKNOWN_RESULT_CODE));
        }

        return result.getOk().orElseThrow(
                () -> CryptoJniException.from(CryptoJniResultCode.ERROR_UNKNOWN_RESULT_CODE));

    }

    private static native String createSecToPubTransfer(String input);

    private static native String generateEncryptedTransfer(String input);
}
