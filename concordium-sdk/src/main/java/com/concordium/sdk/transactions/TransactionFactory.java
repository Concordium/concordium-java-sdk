package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.elgamal.ElgamalPublicKey;
import com.concordium.sdk.crypto.elgamal.ElgamalSecretKey;
import com.concordium.sdk.crypto.encryptedtransfers.EncryptedTransfers;
import com.concordium.sdk.responses.accountinfo.AccountEncryptedAmount;
import com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters;
import lombok.val;

/**
 * TransactionFactory provides convenient functions for building a
 * {@link Transaction}
 */
public class TransactionFactory {

    /**
     * Creates a new {@link TransferTransaction.TransferTransactionBuilder} for
     * creating a {@link TransferTransaction}
     *
     * @return the builder for a {@link TransferTransaction}
     */
    public static TransferTransaction.TransferTransactionBuilder newTransfer() {
        return TransferTransaction.builder();
    }

    /**
     * Creates a new {@link TransferWithMemoTransaction.TransferWithMemoTransactionBuilder} for
     * creating a {@link TransferWithMemoTransaction}
     *
     * @return the builder for a {@link TransferWithMemoTransaction}
     */
    public static TransferWithMemoTransaction.TransferWithMemoTransactionBuilder newTransferWithMemo() {
        return TransferWithMemoTransaction.builder();
    }

    /**
     * Creates a new {@link RegisterDataTransaction.RegisterDataTransactionBuilder} for
     * creating a {@link RegisterDataTransaction}
     *
     * @return the builder for a {@link RegisterDataTransaction}
     */
    public static RegisterDataTransaction.RegisterDataTransactionBuilder newRegisterData() {
        return RegisterDataTransaction.builder();
    }

    /**
     * Creates a new {@link InitContractTransaction.InitContractTransactionBuilder} for
     * creating a {@link InitContractTransaction}
     *
     * @return the builder for a {@link InitContractTransaction}
     */
    public static InitContractTransaction.InitContractTransactionBuilder newInitContract() {
        return InitContractTransaction.builder();
    }


    /**
     * Creates a new {@link UpdateContractTransaction.UpdateContractTransactionBuilder} for
     * creating a {@link UpdateContractTransaction}
     *
     * @return the builder for a {@link UpdateContractTransaction}
     */
    public static UpdateContractTransaction.UpdateContractTransactionBuilder newUpdateContract() {
        return UpdateContractTransaction.builder();
    }

    /**
     * Creates a new {@link DeployModuleTransaction.DeployModuleTransactionBuilder} for
     * creating a {@link DeployModuleTransaction}
     *
     * @return the builder for a {@link DeployModuleTransaction}
     */
    public static DeployModuleTransaction.DeployModuleTransactionBuilder newDeployModule() {
        return DeployModuleTransaction.builder();
    }


    /**
     * Creates a new {@link TransferToPublicTransaction.TransferToPublicTransactionBuilder} for
     * creating a {@link TransferToPublicTransaction}
     *
     * @return the builder for a {@link TransferToPublicTransaction}
     */
    public static TransferToPublicTransaction.TransferToPublicTransactionBuilder newTransferToPublic() {
        return TransferToPublicTransaction.builder();
    }

    /**
     * Creates a new {@link TransferToPublicTransaction.TransferToPublicTransactionBuilder} for
     * creating a {@link TransferToPublicTransaction}
     *
     * @return the builder for a {@link TransferToPublicTransaction}
     */
    public static TransferToPublicTransaction.TransferToPublicTransactionBuilder newTransferToPublic(
            CryptographicParameters cryptographicParameters,
            AccountEncryptedAmount accountEncryptedAmount,
            ElgamalSecretKey accountSecretKey,
            CCDAmount amountToMakePublic) {

        val jniOutput = EncryptedTransfers.createSecToPubTransferPayload(
                cryptographicParameters,
                accountEncryptedAmount,
                accountSecretKey,
                amountToMakePublic
        );

        return TransferToPublicTransaction
                .builder()
                .transferAmount(jniOutput.getTransferAmount())
                .index(jniOutput.getIndex())
                .proof(jniOutput.getProof())
                .remainingAmount(jniOutput.getRemainingAmount());
    }

    /**
     * Creates a new {@link TransferToEncryptedTransaction.TransferToEncryptedTransactionBuilder} for
     * creating a {@link TransferToEncryptedTransaction}
     *
     * @return the builder for a {@link TransferToEncryptedTransaction}
     */
    public static TransferToEncryptedTransaction.TransferToEncryptedTransactionBuilder newTransferToEncrypted() {
        return TransferToEncryptedTransaction.builder();
    }


    /**
     * Creates a new {@link EncryptedTransferTransaction.EncryptedTransferTransactionBuilder} for
     * creating a {@link EncryptedTransferTransaction}
     *
     * @return the builder for a {@link EncryptedTransferTransaction}
     */

    public static EncryptedTransferTransaction.EncryptedTransferTransactionBuilder newEncryptedTransfer() {
        return EncryptedTransferTransaction.builder();
    }


    /**
     * Creates a new {@link EncryptedTransferTransaction.EncryptedTransferTransactionBuilder} for
     * creating a {@link EncryptedTransferTransaction}
     *
     * @return the builder for a {@link EncryptedTransferTransaction}
     */
    public static EncryptedTransferTransaction.EncryptedTransferTransactionBuilder newEncryptedTransfer(
            CryptographicParameters cryptographicParameters,
            AccountEncryptedAmount accountEncryptedAmount,
            ElgamalPublicKey receiverPublicKey,
            ElgamalSecretKey senderSecretKey,
            CCDAmount amountToSend) {
        val jniOutput = EncryptedTransfers.createEncryptedTransferPayload(
                cryptographicParameters,
                accountEncryptedAmount,
                receiverPublicKey,
                senderSecretKey,
                amountToSend
        );

        val amountTransferData = EncryptedAmountTransferData.builder()
                .remainingAmount(jniOutput.getRemainingAmount())
                .transferAmount(jniOutput.getTransferAmount())
                .index(jniOutput.getIndex())
                .proof(jniOutput.getProof()).build();

        return EncryptedTransferTransaction
                .builder()
                .data(amountTransferData);

    }

    /**
     * Creates a new {@link EncryptedTransferWithMemoTransaction.EncryptedTransferWithMemoTransactionBuilder} for
     * creating a {@link EncryptedTransferWithMemoTransaction}
     *
     * @return the builder for a {@link EncryptedTransferWithMemoTransaction}
     */

    public static EncryptedTransferWithMemoTransaction.EncryptedTransferWithMemoTransactionBuilder newEncryptedTransferWithMemo() {
        return EncryptedTransferWithMemoTransaction.builder();
    }

    /**
     * Creates a new {@link EncryptedTransferWithMemoTransaction.EncryptedTransferWithMemoTransactionBuilder} for
     * creating a {@link EncryptedTransferWithMemoTransaction}
     *
     * @return the builder for a {@link EncryptedTransferWithMemoTransaction}
     */
    public static EncryptedTransferWithMemoTransaction.EncryptedTransferWithMemoTransactionBuilder newEncryptedTransferWithMemo(
            CryptographicParameters cryptographicParameters,
            AccountEncryptedAmount accountEncryptedAmount,
            ElgamalPublicKey receiverPublicKey,
            ElgamalSecretKey senderSecretKey,
            CCDAmount amountToSend) {
        val jniOutput = EncryptedTransfers.createEncryptedTransferPayload(
                cryptographicParameters,
                accountEncryptedAmount,
                receiverPublicKey,
                senderSecretKey,
                amountToSend
        );

        val amountTransferData = EncryptedAmountTransferData.builder()
                .remainingAmount(jniOutput.getRemainingAmount())
                .transferAmount(jniOutput.getTransferAmount())
                .index(jniOutput.getIndex())
                .proof(jniOutput.getProof()).build();

        return EncryptedTransferWithMemoTransaction
                .builder()
                .data(amountTransferData);

    }


}
