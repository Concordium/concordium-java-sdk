package com.concordium.sdk.responses.transactionevent.accounttransactionresults;

import com.concordium.grpc.v2.AccountAddress;
import com.concordium.grpc.v2.AccountTransactionEffects;
import com.concordium.sdk.responses.transactionstatus.*;
import lombok.val;

public interface AccountTransactionResult extends TransactionResultEvent {

    /**
     * Parses {@link AccountTransactionEffects} and {@link AccountAddress} to {@link AccountTransactionResult}.
     * @param effects {@link AccountTransactionEffects} returned by the GRPC V2 API.
     * @param sender {@link AccountAddress} returned by the GRPC V2 API.
     * @return parsed {@link AccountTransactionResult}.
     */
    static AccountTransactionResult parse(AccountTransactionEffects effects, AccountAddress sender) {

        switch (effects.getEffectCase()) {
            case NONE:
                return NoneResult.parse(effects.getNone());
            case MODULE_DEPLOYED:
                return ModuleDeployedResult.parse(effects.getModuleDeployed());
            case CONTRACT_INITIALIZED:
                return ContractInitializedResult.parse(effects.getContractInitialized());
            case CONTRACT_UPDATE_ISSUED:
                return ContractUpdateIssuedResult.parse(effects.getContractUpdateIssued());
            case ACCOUNT_TRANSFER:
                return AccountTransferResult.parse(effects.getAccountTransfer());
            case BAKER_ADDED:
                return BakerAddedResult.parse(effects.getBakerAdded());
            case BAKER_REMOVED:
                return BakerRemovedResult.parse(effects.getBakerRemoved(), sender);
            case BAKER_STAKE_UPDATED:
                val bakerStakeUpdated = effects.getBakerStakeUpdated();
                if (!bakerStakeUpdated.hasUpdate()) {break;} // If the stake was not updated nothing happened.
                if (bakerStakeUpdated.getUpdate().getIncreased()) {
                    return BakerStakeIncreasedResult.parse(bakerStakeUpdated.getUpdate(), sender);
                } else {
                    return BakerStakeDecreasedResult.parse(bakerStakeUpdated.getUpdate(), sender);
                }
            case BAKER_RESTAKE_EARNINGS_UPDATED:
                return BakerSetRestakeEarningsResult.parse(effects.getBakerRestakeEarningsUpdated(), sender);
            case BAKER_KEYS_UPDATED:
                return  BakerKeysUpdatedResult.parse(effects.getBakerKeysUpdated());
            case ENCRYPTED_AMOUNT_TRANSFERRED:
                return EncryptedAmountTransferredResult.parse(effects.getEncryptedAmountTransferred());
            case TRANSFERRED_TO_ENCRYPTED:
                return EncryptedSelfAmountAddedResult.parse(effects.getTransferredToEncrypted());
            case TRANSFERRED_TO_PUBLIC:
                return TransferredToPublicResult.parse(effects.getTransferredToPublic());
            case TRANSFERRED_WITH_SCHEDULE:
                // TODO handle memo
                break;
            case CREDENTIAL_KEYS_UPDATED:
                return CredentialKeysUpdatedResult.parse(effects.getCredentialKeysUpdated());
            case CREDENTIALS_UPDATED:
                return CredentialsUpdatedResult.parse(effects.getCredentialsUpdated(), sender);
            case DATA_REGISTERED:
                return DataRegisteredResult.parse(effects.getDataRegistered());
            case BAKER_CONFIGURED:
                return BakerConfiguredResult.parse(effects.getBakerConfigured(), sender);
            case DELEGATION_CONFIGURED:
                return DelegationConfiguredResult.parse(effects.getDelegationConfigured(), sender);
            case EFFECT_NOT_SET:
                throw new IllegalArgumentException();
        }

        return null;
    }
}
