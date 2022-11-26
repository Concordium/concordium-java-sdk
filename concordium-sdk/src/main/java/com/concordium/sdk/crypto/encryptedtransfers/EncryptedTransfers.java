package com.concordium.sdk.crypto.encryptedtransfers;

import com.concordium.sdk.crypto.NativeResolver;
import com.concordium.sdk.crypto.elgamal.ElgamalSecretKey;
import com.concordium.sdk.exceptions.EncryptedTransfersException;
import com.concordium.sdk.responses.accountinfo.AccountEncryptedAmount;
import com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters;
import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.val;

import java.io.IOException;

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

        EncryptedTransfersResult result = null;
        try {
            val inputJsonString = JsonMapper.INSTANCE.writeValueAsString(jniInput);
            val jsonStr = createSecToPubTransfer(inputJsonString);
            result = JsonMapper.INSTANCE.readValue(jsonStr, EncryptedTransfersResult.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (!result.isok()) {
            throw EncryptedTransfersException.from(
                    result.getErr().orElse(EncryptedTransfersResultCode.ERROR_UNKNOWN_RESULT_CODE));
        }

        return result.getOk().orElseThrow(
                () -> EncryptedTransfersException.from(EncryptedTransfersResultCode.ERROR_UNKNOWN_RESULT_CODE));
    }


    public static EncryptedTransferJniOutput createEncryptedTransferPayload(
            CryptographicParameters cryptographicParameters,
            AccountEncryptedAmount inputAmount,
            String receiverPublicKey,
            String senderSecretKey,
            String amountToSend) {
        return createEncryptedTransferPayload(EncryptedAmountTransferDataJniInput.builder()
                .global(GlobalContext.from(cryptographicParameters))
                .receiverPublicKey(receiverPublicKey)
                .senderSecretKey(senderSecretKey)
                .amountToSend(amountToSend)
                .inputEncryptedAmount(IndexedEncryptedAmount.from(inputAmount))
                .build());
    }

    static EncryptedTransferJniOutput createEncryptedTransferPayload(EncryptedAmountTransferDataJniInput jniInput) {

        EncryptedTransferJniOutput result = null;
        try {
            val inputJsonString = JsonMapper.INSTANCE.writeValueAsString(jniInput);
            val buff = new byte[10000];
            val resultCode = generateEncryptedTransfer(inputJsonString, buff);
            if (resultCode > 0) {
                val errString = new java.lang.String(buff);
                throw new Exception("Error: " + errString + ", ErrorCode: " + resultCode);
            }
            val jsonStr = new java.lang.String(buff);
            result = JsonMapper.INSTANCE.readValue(jsonStr, EncryptedTransferJniOutput.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private static native String createSecToPubTransfer(String input);

    private static native int generateEncryptedTransfer(String input, byte[] buffer);
}
