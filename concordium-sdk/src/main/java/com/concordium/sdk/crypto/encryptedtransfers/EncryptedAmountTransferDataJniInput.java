package com.concordium.sdk.crypto.encryptedtransfers;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EncryptedAmountTransferDataJniInput {
    private final GlobalContext global;
    private final String receiverPublicKey;
    private final String senderSecretKey;
    private final String amountToSend;
    private final IndexedEncryptedAmount inputEncryptedAmount;
}
