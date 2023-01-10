package com.concordium.sdk.transactions;

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
     * Creates a new {@link TransferScheduleTransaction.TransferScheduleTransactionBuilder} for
     * creating a {@link TransferScheduleTransaction}
     *
     * @return the builder for a {@link TransferScheduleTransaction}
     */
    public static TransferScheduleTransaction.TransferScheduleTransactionBuilder newScheduledTransfer() {
        return TransferScheduleTransaction.builder();
    }

    /**
     * Creates a new {@link TransferScheduleWithMemoTransaction.TransferScheduleWithMemoTransactionBuilder} for
     * creating a {@link TransferScheduleWithMemoTransaction}
     *
     * @return the builder for a {@link TransferScheduleWithMemoTransaction}
     */
    public static TransferScheduleWithMemoTransaction.TransferScheduleWithMemoTransactionBuilder newScheduledTransferWithMemo() {
        return TransferScheduleWithMemoTransaction.builder();
    }

    /**
     * Creates a new {@link UpdateCredentialKeysTransaction.UpdateCredentialKeysTransactionBuilder} for
     * creating a {@link UpdateCredentialKeysTransaction}
     *
     * @return the builder for a {@link UpdateCredentialKeysTransaction}
     */
    public static UpdateCredentialKeysTransaction.UpdateCredentialKeysTransactionBuilder newUpdateCredentialKeys() {
        return UpdateCredentialKeysTransaction.builder();
    }
}
