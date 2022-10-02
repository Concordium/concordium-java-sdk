package com.concordium.sdk.crypto.encryptedtransfers;

import com.concordium.sdk.crypto.elgamal.ElgamalSecretKey;
import com.concordium.sdk.transactions.CCDAmount;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
class TransferToPublicJniInput {
    private final GlobalContext global;
    private final CCDAmount amount;
    private final ElgamalSecretKey senderSecretKey;
    private final IndexedEncryptedAmount inputEncryptedAmount;
}
