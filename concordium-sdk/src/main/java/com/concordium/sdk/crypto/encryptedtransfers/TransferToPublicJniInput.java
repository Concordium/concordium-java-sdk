package com.concordium.sdk.crypto.encryptedtransfers;

import com.concordium.sdk.crypto.elgamal.ElgamalSecretKey;
import com.concordium.sdk.transactions.CCDAmount;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
class TransferToPublicJniInput {
    /**
     * Global context with parameters for generating proofs, and generators for encrypting amounts
     */
    private final GlobalContext global;
    /**
     * Amount to send
     */
    private final CCDAmount amount;
    /**
     * Secret key of the sender (who is also the receiver)
     */
    private final ElgamalSecretKey senderSecretKey;
    /**
     * input amount from which to send
     */
    private final IndexedEncryptedAmount inputEncryptedAmount;
}
