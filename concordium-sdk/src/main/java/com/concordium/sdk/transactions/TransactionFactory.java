package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.bakertransactions.BakerKeys;
import com.concordium.sdk.crypto.elgamal.ElgamalSecretKey;
import com.concordium.sdk.crypto.encryptedtransfers.EncryptedTransfers;
import com.concordium.sdk.responses.accountinfo.AccountEncryptedAmount;
import com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters;
import com.concordium.sdk.transactions.smartcontracts.WasmModule;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.Nonce;
import com.concordium.sdk.types.UInt16;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.NonNull;
import lombok.val;

/**
 * TransactionFactory provides convenient functions for building a
 * {@link AccountTransaction}
 */
@SuppressWarnings("unused")
public class TransactionFactory {

    @Builder(
            builderMethodName = "newTransfer",
            builderClassName = "TransferAccountTransactionBuilder"
    )
    private static AccountTransaction transferBuilder(@NonNull AccountAddress sender,
                                                      @NonNull AccountAddress receiver,
                                                      @NonNull CCDAmount amount,
                                                      @NonNull Nonce nonce,
                                                      @NonNull Expiry expiry,
                                                      @NonNull TransactionSigner signer) {
        val payload = new Transfer(receiver, amount);
        val cost = TransactionTypeCost.TRANSFER_BASE_COST.getValue();
        return AccountTransaction.from(sender, nonce, expiry, signer, payload, cost);
    }

    @Builder(
            builderMethodName = "newTransferWithMemo",
            builderClassName = "TransferWithMemoAccountTransactionBuilder"
    )
    private static AccountTransaction transferWithMemoBuilder(@NonNull AccountAddress sender,
                                                              @NonNull AccountAddress receiver,
                                                              @NonNull CCDAmount amount,
                                                              @NonNull Memo memo,
                                                              @NonNull Nonce nonce,
                                                              @NonNull Expiry expiry,
                                                              @NonNull TransactionSigner signer) {
        val payload = new TransferWithMemo(receiver, amount, memo);
        val cost = TransactionTypeCost.TRANSFER_WITH_MEMO.getValue();
        return AccountTransaction.from(sender, nonce, expiry, signer, payload, cost);
    }

    @Builder(
            builderMethodName = "newRegisterData",
            builderClassName = "RegisterDataAccountTransactionBuilder"
    )
    private static AccountTransaction registerDataBuilder(@NonNull AccountAddress sender,
                                                          @NonNull Data data,
                                                          @NonNull Nonce nonce,
                                                          @NonNull Expiry expiry,
                                                          @NonNull TransactionSigner signer) {
        val payload = new RegisterData(data);
        val cost = TransactionTypeCost.REGISTER_DATA.getValue();
        return AccountTransaction.from(sender, nonce, expiry, signer, payload, cost);
    }

    @Builder(
            builderMethodName = "newInitContract",
            builderClassName = "InitContractAccountTransactionBuilder"
    )
    private static AccountTransaction initContractBuilder(@NonNull InitContract payload,
                                                          @NonNull AccountAddress sender,
                                                          @NonNull Nonce nonce,
                                                          @NonNull Expiry expiry,
                                                          @NonNull TransactionSigner signer,
                                                          @NonNull UInt64 maxContractExecutionEnergy) {
        return AccountTransaction.from(sender, nonce, expiry, signer, payload, maxContractExecutionEnergy);
    }

    @Builder(
            builderMethodName = "newUpdateContract",
            builderClassName = "UpdateContractAccountTransactionBuilder"
    )
    private static AccountTransaction updateContractBuilder(@NonNull UpdateContract payload,
                                                            @NonNull AccountAddress sender,
                                                            @NonNull Nonce nonce,
                                                            @NonNull Expiry expiry,
                                                            @NonNull TransactionSigner signer,
                                                            @NonNull UInt64 maxContractExecutionEnergy) {
        return AccountTransaction.from(sender, nonce, expiry, signer, payload, maxContractExecutionEnergy);
    }

    @Builder(
            builderMethodName = "newDeployModule",
            builderClassName = "DeployModuleAccountTransactionBuilder"
    )
    private static AccountTransaction deployModuleBuilder(@NonNull AccountAddress sender,
                                                          @NonNull Nonce nonce,
                                                          @NonNull Expiry expiry,
                                                          @NonNull TransactionSigner signer,
                                                          @NonNull WasmModule module,
                                                          @NonNull UInt64 maxContractExecutionEnergy) {
        val payload = new DeployModule(module);
        return AccountTransaction.from(sender, nonce, expiry, signer, payload, maxContractExecutionEnergy);
    }

    @Builder(
            builderMethodName = "newScheduledTransfer",
            builderClassName = "ScheduledTransferAccountTransactionBuilder"
    )
    private static AccountTransaction scheduledTransferBuilder(@NonNull AccountAddress sender,
                                                               @NonNull AccountAddress to,
                                                               @NonNull Schedule[] schedule,
                                                               @NonNull Nonce nonce,
                                                               @NonNull Expiry expiry,
                                                               @NonNull TransactionSigner signer) {
        val payload = new TransferSchedule(to, schedule);
        val cost = UInt64.from((long) schedule.length * (300 + 64));
        return AccountTransaction.from(sender, nonce, expiry, signer, payload, cost);
    }

    @Builder(
            builderMethodName = "newScheduledTransferWithMemo",
            builderClassName = "ScheduledTransferWithMemoAccountTransactionBuilder"
    )
    private static AccountTransaction scheduledTransferWithMemoBuilder(@NonNull AccountAddress sender,
                                                                       @NonNull AccountAddress to,
                                                                       @NonNull Schedule[] schedule,
                                                                       @NonNull Memo memo,
                                                                       @NonNull Nonce nonce,
                                                                       @NonNull Expiry expiry,
                                                                       @NonNull TransactionSigner signer) {
        val payload = new TransferScheduleWithMemo(to, schedule, memo);
        val cost = UInt64.from((long) schedule.length * (300 + 64));
        return AccountTransaction.from(sender, nonce, expiry, signer, payload, cost);
    }

    @Builder(
            builderMethodName = "newUpdateCredentialKeys",
            builderClassName = "UpdateCredentialKeysAccountTransactionBuilder"
    )
    private static AccountTransaction updateCredentialKeysBuilder(@NonNull CredentialRegistrationId credentialRegistrationID,
                                                                  @NonNull CredentialPublicKeys keys,
                                                                  @NonNull UInt16 numExistingCredentials,
                                                                  @NonNull AccountAddress sender,
                                                                  @NonNull Nonce nonce,
                                                                  @NonNull Expiry expiry,
                                                                  @NonNull TransactionSigner signer) {
        val payload = new UpdateCredentialKeys(credentialRegistrationID, keys, numExistingCredentials);
        val cost = UInt64.from(500L * numExistingCredentials.getValue() + 100L * keys.getKeys().size());
        return AccountTransaction.from(sender, nonce, expiry, signer, payload, cost);
    }

    /**
     * @see TransactionFactory#newTransferToPublicWithSecretKey(CryptographicParameters, AccountEncryptedAmount, ElgamalSecretKey, CCDAmount)
     */
    @Builder(
            builderMethodName = "newTransferToPublic",
            builderClassName = "TransferToPublicAccountTransactionBuilder"
    )
    private static AccountTransaction transferToPublicBuilder(@NonNull EncryptedAmount remainingAmount,
                                                              @NonNull CCDAmount transferAmount,
                                                              @NonNull UInt64 index,
                                                              @NonNull SecToPubAmountTransferProof proof,
                                                              @NonNull AccountAddress sender,
                                                              @NonNull Nonce nonce,
                                                              @NonNull Expiry expiry,
                                                              @NonNull TransactionSigner signer) {
        val payload = new TransferToPublic(remainingAmount, transferAmount, index, proof);
        val cost = TransactionTypeCost.TRANSFER_TO_PUBLIC.getValue();
        return AccountTransaction.from(sender, nonce, expiry, signer, payload, cost);
    }

    public static TransferToPublicAccountTransactionBuilder newTransferToPublicWithSecretKey(CryptographicParameters cryptographicParameters,
                                                                                             AccountEncryptedAmount accountEncryptedAmount,
                                                                                             ElgamalSecretKey accountSecretKey,
                                                                                             CCDAmount amountToMakePublic) {
        val jniOutput = EncryptedTransfers.createSecToPubTransferPayload(
                cryptographicParameters,
                accountEncryptedAmount,
                accountSecretKey,
                amountToMakePublic
        );

        return newTransferToPublic()
                .transferAmount(jniOutput.getTransferAmount())
                .index(jniOutput.getIndex())
                .proof(jniOutput.getProof())
                .remainingAmount(jniOutput.getRemainingAmount());
    }

    @Builder(
            builderMethodName = "newConfigureBaker",
            builderClassName = "ConfigureBakerAccountTransactionBuilder"
    )
    private static AccountTransaction configureBakerBuilder(@NonNull ConfigureBaker payload,
                                                            @NonNull AccountAddress sender,
                                                            @NonNull Nonce nonce,
                                                            @NonNull Expiry expiry,
                                                            @NonNull TransactionSigner signer) {
        val cost = (payload.getKeysWithProofs() != null)
                ? TransactionTypeCost.CONFIGURE_BAKER_WITH_PROOFS.getValue()
                : TransactionTypeCost.CONFIGURE_BAKER.getValue();
        return AccountTransaction.from(sender, nonce, expiry, signer, payload, cost);
    }

    @Builder(
            builderMethodName = "newRemoveBaker",
            builderClassName = "RemoveBakerAccountTransactionBuilder"
    )
    private static AccountTransaction removeBakerBuilder(@NonNull AccountAddress sender,
                                                         @NonNull Nonce nonce,
                                                         @NonNull Expiry expiry,
                                                         @NonNull TransactionSigner signer) {
        val payload = ConfigureBaker.builder()
                .capital(CCDAmount.fromMicro(0))
                .build();
        val cost = TransactionTypeCost.CONFIGURE_BAKER.getValue();
        return AccountTransaction.from(sender, nonce, expiry, signer, payload, cost);
    }

    @Builder(
            builderMethodName = "newUpdateBakerStake",
            builderClassName = "UpdateBakerStakeAccountTransactionBuilder"
    )
    private static AccountTransaction updateBakerStakeBuilder(@NonNull CCDAmount stake,
                                                              @NonNull AccountAddress sender,
                                                              @NonNull Nonce nonce,
                                                              @NonNull Expiry expiry,
                                                              @NonNull TransactionSigner signer) {
        val payload = ConfigureBaker.builder()
                .capital(stake)
                .build();
        val cost = TransactionTypeCost.CONFIGURE_BAKER.getValue();
        return AccountTransaction.from(sender, nonce, expiry, signer, payload, cost);
    }

    @Builder(
            builderMethodName = "newUpdateBakerRestakeEarnings",
            builderClassName = "UpdateBakerRestakeEarningsAccountTransactionBuilder"
    )
    private static AccountTransaction updateBakerRestakeEarningsBuilder(boolean restakeEarnings,
                                                                        @NonNull AccountAddress sender,
                                                                        @NonNull Nonce nonce,
                                                                        @NonNull Expiry expiry,
                                                                        @NonNull TransactionSigner signer) {
        val payload = ConfigureBaker.builder()
                .restakeEarnings(restakeEarnings)
                .build();
        val cost = TransactionTypeCost.CONFIGURE_BAKER.getValue();
        return AccountTransaction.from(sender, nonce, expiry, signer, payload, cost);
    }

    @Builder(
            builderMethodName = "newUpdateBakerKeys",
            builderClassName = "UpdateBakerKeysAccountTransactionBuilder"
    )
    private static AccountTransaction updateBakerKeysBuilder(@NonNull AccountAddress accountAddress,
                                                             @NonNull BakerKeys bakerKeys,
                                                             @NonNull AccountAddress sender,
                                                             @NonNull Nonce nonce,
                                                             @NonNull Expiry expiry,
                                                             @NonNull TransactionSigner signer) {
        val payload = ConfigureBaker.builder()
                .keysWithProofs(ConfigureBakerKeysPayload.getNewConfigureBakerKeysPayload(accountAddress, bakerKeys))
                .build();
        val cost = TransactionTypeCost.CONFIGURE_BAKER_WITH_PROOFS.getValue();
        return AccountTransaction.from(sender, nonce, expiry, signer, payload, cost);
    }

    @Builder(
            builderMethodName = "newConfigureDelegation",
            builderClassName = "ConfigureDelegationAccountTransactionBuilder"
    )
    private static AccountTransaction configureDelegationBuilder(@NonNull ConfigureDelegation payload,
                                                                 @NonNull AccountAddress sender,
                                                                 @NonNull Nonce nonce,
                                                                 @NonNull Expiry expiry,
                                                                 @NonNull TransactionSigner signer) {
        val cost = TransactionTypeCost.CONFIGURE_DELEGATION.getValue();
        return AccountTransaction.from(sender, nonce, expiry, signer, payload, cost);
    }

    @Builder(
            builderMethodName = "newTokenUpdate",
            builderClassName = "TokenUpdateAccountTransactionBuilder"
    )
    private static AccountTransaction tokenUpdateBuilder(@NonNull TokenUpdate payload,
                                                         @NonNull AccountAddress sender,
                                                         @NonNull Nonce nonce,
                                                         @NonNull Expiry expiry,
                                                         @NonNull TransactionSigner signer) {
        val cost = TransactionTypeCost.TOKEN_UPDATE_BASE_COST.getValue().plus(payload.getOperationsBaseCost());
        return AccountTransaction.from(sender, nonce, expiry, signer, payload, cost);
    }
}
