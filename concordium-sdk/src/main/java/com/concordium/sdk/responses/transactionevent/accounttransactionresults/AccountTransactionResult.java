package com.concordium.sdk.responses.transactionevent.accounttransactionresults;

import com.concordium.grpc.v2.AccountAddress;
import com.concordium.grpc.v2.AccountTransactionEffects;
import com.concordium.sdk.responses.transactionstatus.*;
import lombok.val;

/**
 * Result of ann account transaction.
 * All variants except {@link NoneResult} correspond to a unique transaction that was successful.
 */
public interface AccountTransactionResult {

    TransactionType getResultType();

    /**
     * Parses {@link AccountTransactionEffects} and {@link AccountAddress} to {@link AccountTransactionResult}.
     * @param effects {@link AccountTransactionEffects} returned by the GRPC V2 API.
     * @param sender {@link AccountAddress} returned by the GRPC V2 API.
     * @return parsed {@link AccountTransactionResult}.
     */
    static AccountTransactionResult parse(AccountTransactionEffects effects, AccountAddress sender) {

        switch (effects.getEffectCase()) {
            case NONE: return NoneResult.parse(effects.getNone());
            case MODULE_DEPLOYED: return ModuleDeployedResult.parse(effects.getModuleDeployed());
            case CONTRACT_INITIALIZED: return ContractInitializedResult.parse(effects.getContractInitialized());
            case CONTRACT_UPDATE_ISSUED: return ContractUpdateIssuedResult.parse(effects.getContractUpdateIssued());
            case ACCOUNT_TRANSFER:
                val transfer = effects.getAccountTransfer();
                if (transfer.hasMemo()) {return AccountTransferWithMemoResult.parse(transfer);}
                return AccountTransferResult.parse(transfer);
            case BAKER_ADDED: return BakerAddedResult.parse(effects.getBakerAdded());
            case BAKER_REMOVED: return BakerRemovedResult.parse(effects.getBakerRemoved(), sender);
            case BAKER_STAKE_UPDATED: return BakerStakeUpdatedResult.parse(effects.getBakerStakeUpdated());
            case BAKER_RESTAKE_EARNINGS_UPDATED: return BakerSetRestakeEarningsResult.parse(effects.getBakerRestakeEarningsUpdated(), sender);
            case BAKER_KEYS_UPDATED: return  BakerKeysUpdatedResult.parse(effects.getBakerKeysUpdated());
            case ENCRYPTED_AMOUNT_TRANSFERRED:
                val encryptedTransfer = effects.getEncryptedAmountTransferred();
                if (encryptedTransfer.hasMemo()) {
                    return EncryptedAmountTransferredWithMemoResult.parse(encryptedTransfer);
                }
                return EncryptedAmountTransferredResult.parse(encryptedTransfer);
            case TRANSFERRED_TO_ENCRYPTED: return EncryptedSelfAmountAddedResult.parse(effects.getTransferredToEncrypted());
            case TRANSFERRED_TO_PUBLIC: return TransferredToPublicResult.parse(effects.getTransferredToPublic());
            case TRANSFERRED_WITH_SCHEDULE:
                val transferredWithSchedule = effects.getTransferredWithSchedule();
                if (transferredWithSchedule.hasMemo()) {
                    return TransferredWithScheduleAndMemoResult.parse(transferredWithSchedule, sender);
                }
                return TransferredWithScheduleResult.parse(transferredWithSchedule, sender);
            case CREDENTIAL_KEYS_UPDATED: return CredentialKeysUpdatedResult.parse(effects.getCredentialKeysUpdated());
            case CREDENTIALS_UPDATED: return CredentialsUpdatedResult.parse(effects.getCredentialsUpdated(), sender);
            case DATA_REGISTERED: return DataRegisteredResult.parse(effects.getDataRegistered());
            case BAKER_CONFIGURED: return BakerConfiguredResult.parse(effects.getBakerConfigured(), sender);
            case DELEGATION_CONFIGURED: return DelegationConfiguredResult.parse(effects.getDelegationConfigured(), sender);
            default: throw new IllegalArgumentException();
        }
    }
}
