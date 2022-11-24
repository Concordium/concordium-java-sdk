package com.concordium.sdk.transactions;

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


    public static TransferToPublicTransaction.TransferToPublicTransactionBuilder newTransferToPublic() {
        return TransferToPublicTransaction.builder();
    }

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
     * Creates a new {@link ConfigureBakerTransaction.ConfigureBakerTransactionBuilder} for
     * creating a {@link ConfigureBakerTransaction}
     *
     * @return the builder for a {@link ConfigureBakerTransaction}
     */
    public static ConfigureBakerTransaction.ConfigureBakerTransactionBuilder newConfigureBaker() {
        return ConfigureBakerTransaction.builder();
    }

    public static ConfigureBakerTransaction.ConfigureBakerTransactionBuilder newRemoveBaker() {
        val payload = ConfigureBakerPayload.builder()
                .capital(CCDAmount.fromMicro(0))
                .build();
        return ConfigureBakerTransaction.builder().payload(payload);
    }

    public static ConfigureBakerTransaction.ConfigureBakerTransactionBuilder newUpdateBakerStake(long stake) {
        val payload = ConfigureBakerPayload.builder()
                .capital(CCDAmount.fromMicro(stake))
                .build();
        return ConfigureBakerTransaction.builder().payload(payload);
    }

    public static ConfigureBakerTransaction.ConfigureBakerTransactionBuilder newUpdateBakerRestakeEarnings(boolean restakeEarnings) {
        val payload = ConfigureBakerPayload.builder()
                .restakeEarnings(restakeEarnings)
                .build();
        return ConfigureBakerTransaction.builder().payload(payload);
    }

    public static ConfigureBakerTransaction.ConfigureBakerTransactionBuilder newUpdateBakerKeys(AccountAddress accountAddress) {
        val payload = ConfigureBakerPayload.builder()
                .keysWithProofs(ConfigureBakerKeysPayload.newBakerKeysWithPayload(accountAddress))
                .build();
        return ConfigureBakerTransaction.builder().payload(payload);
    }
}
