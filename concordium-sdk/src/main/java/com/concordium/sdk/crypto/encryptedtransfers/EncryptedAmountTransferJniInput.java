package com.concordium.sdk.crypto.encryptedtransfers;

import com.concordium.sdk.crypto.elgamal.ElgamalPublicKey;
import com.concordium.sdk.crypto.elgamal.ElgamalSecretKey;
import com.concordium.sdk.transactions.CCDAmount;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EncryptedAmountTransferJniInput {
    /**
     * Global context with parameters for generating proofs, and generators for encrypting amounts
     */
    private final GlobalContext global;
    /**
     * Public key of the receiver of the transfer
     */
    private final ElgamalPublicKey receiverPublicKey;
    /**
     * Secret key of the sender of the transfer
     */
    private final ElgamalSecretKey senderSecretKey;
    /**
     * Amount to send
     */
    private final IndexedEncryptedAmount inputEncryptedAmount;
    /**
     * Input amount from which to send
     */
    private final CCDAmount amountToSend;
}
