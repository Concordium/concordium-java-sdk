package com.concordium.sdk.transactions;

import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.transactions.smartcontracts.WasmModule;
import com.concordium.sdk.types.UInt64;
import lombok.*;

/**
 * A {@link DeployModuleTransaction} deploys compiled WASM smart contract module to chain.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DeployModuleTransaction extends AbstractAccountTransaction {
    private DeployModuleTransaction(
            @NonNull final AccountAddress sender,
            @NonNull final AccountNonce nonce,
            @NonNull final Expiry expiry,
            @NonNull final TransactionSigner signer,
            @NonNull final WasmModule module,
            @NonNull final UInt64 maxEnergyCost) {
        super(sender, nonce, expiry, signer, DeployModule.createNew(module, maxEnergyCost));
    }

    private DeployModuleTransaction(
            final @NonNull TransactionHeader header,
            final @NonNull TransactionSignature signature,
            final @NonNull WasmModule payload) {
        super(header, signature, TransactionType.DEPLOY_MODULE, payload.getBytes());
    }

    @Builder(builderClassName = "DeployModuleTransactionBuilder")
    public static DeployModuleTransaction from(final AccountAddress sender,
                                               final AccountNonce nonce,
                                               final Expiry expiry,
                                               final TransactionSigner signer,
                                               final WasmModule module,
                                               final UInt64 maxEnergyCost) {
        try {
            return new DeployModuleTransaction(sender, nonce, expiry, signer, module, maxEnergyCost);
        } catch (NullPointerException nullPointerException) {
            throw TransactionCreationException.from(nullPointerException);
        }
    }

    /**
     * Creates a new instance of {@link DeployModuleTransaction}.
     * Using {@link TransactionHeader}, {@link TransactionSignature} and Payload {@link WasmModule}.
     *
     * @param header    {@link TransactionHeader}.
     * @param signature {@link TransactionSignature}.
     * @param payload   {@link WasmModule} Payload for this transaction.
     * @return Instantiated {@link DeployModuleTransaction}.
     */
    @Builder(builderMethodName = "builderBlockItem", builderClassName = "DeployModuleBlockItemBuilder")
    static DeployModuleTransaction from(
            final TransactionHeader header,
            final TransactionSignature signature,
            final WasmModule payload) {
        try {
            return new DeployModuleTransaction(header, signature, payload);
        } catch (NullPointerException nullPointerException) {
            throw TransactionCreationException.from(nullPointerException);
        }
    }
}
