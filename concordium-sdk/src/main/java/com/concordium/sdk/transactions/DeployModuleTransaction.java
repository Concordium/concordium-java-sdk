package com.concordium.sdk.transactions;

import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.transactions.smartcontracts.WasmModule;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.UInt64;
import lombok.*;

/**
 * A {@link DeployModuleTransaction} deploys compiled WASM smart contract module to chain.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DeployModuleTransaction extends AccountTransaction {
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
            final @NonNull WasmModule payload,
            final @NonNull UInt64 maxEnergyCost) {
        super(header, signature, DeployModule.createNew(payload, maxEnergyCost));
    }

    /**
     * @param sender        Sender ({@link AccountAddress}) of this Transaction.
     * @param nonce         Account {@link com.concordium.sdk.types.Nonce} Of the Sender Account.
     * @param expiry        {@link Expiry} of this transaction.
     * @param signer        {@link Signer} of this transaction.
     * @param module        {@link WasmModule} Compiled Source code of the Smart Contract Module.
     * @param maxEnergyCost Energy allowed for this transaction.
     * @return Initialized {@link DeployModuleTransaction}
     * @throws TransactionCreationException On failure to create the Transaction from input params.
     *                                      Ex when any of the input param is NULL.
     */
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
     * @throws TransactionCreationException On failure to create the Transaction from input params.
     *                                      Ex when any of the input param is NULL.
     */
    @Builder(builderMethodName = "builderBlockItem", builderClassName = "DeployModuleBlockItemBuilder")
    static DeployModuleTransaction from(
            final TransactionHeader header,
            final TransactionSignature signature,
            final WasmModule payload,
            final UInt64 maxEnergyCost) {
        try {
            return new DeployModuleTransaction(header, signature, payload, maxEnergyCost);
        } catch (NullPointerException nullPointerException) {
            throw TransactionCreationException.from(nullPointerException);
        }
    }
}
