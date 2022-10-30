package com.concordium.sdk.crypto.encryptedtransfers;

import com.concordium.sdk.crypto.CryptoJniResultCode;
import com.concordium.sdk.crypto.NativeResolver;
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
            throw CryptoJniException.from(
                    result.getErr().orElse(CryptoJniResultCode.ERROR_UNKNOWN_RESULT_CODE));
        }

        return result.getOk().orElseThrow(
                () -> CryptoJniException.from(CryptoJniResultCode.ERROR_UNKNOWN_RESULT_CODE));
    }

    private static native String createSecToPubTransfer(String input);
}
