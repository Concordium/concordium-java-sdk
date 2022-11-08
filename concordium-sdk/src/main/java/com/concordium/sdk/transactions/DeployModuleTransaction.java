package com.concordium.sdk.transactions;

import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.transactions.smartcontracts.WasmModule;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.Getter;
import lombok.val;

import java.util.Objects;

/**
 * A {@link DeployModuleTransaction} deploys compiled WASM smart contract module to chain.
 */
@Getter
class DeployModuleTransaction extends AbstractTransaction {
    /**
     * A compiled Smart Contract Module in WASM with source and version.
     */
    private final WasmModule module;

    private final AccountAddress sender;
    private final AccountNonce nonce;
    private final Expiry expiry;
    private final TransactionSigner signer;
    private final UInt64 maxEnergyCost;
    private BlockItem blockItem;

    @Builder
    DeployModuleTransaction(
            AccountAddress sender,
            AccountNonce nonce,
            Expiry expiry,
            TransactionSigner signer,
            WasmModule module,
            UInt64 maxEnergyCost) throws TransactionCreationException {
        this.sender = sender;
        this.nonce = nonce;
        this.expiry = expiry;
        this.signer = signer;
        this.module = module;
        this.maxEnergyCost = maxEnergyCost;
    }

    /**
     * This function returns a new instance of the DeployModuleTransaction class.
     */
    public static DeployModuleTransaction.DeployModuleTransactionBuilder builder() {
        return new DeployModuleTransaction.CustomBuilder();
    }

    /**
     * This function returns the block item associated with this block.
     */
    @Override
    public BlockItem getBlockItem() {
        return blockItem;
    }

    private static class CustomBuilder extends DeployModuleTransaction.DeployModuleTransactionBuilder {
        static void verifyDeployModuleInput(
                AccountAddress sender,
                AccountNonce nonce,
                Expiry expiry,
                WasmModule module,
                TransactionSigner signer) throws TransactionCreationException {
            Transaction.verifyAccountTransactionHeaders(sender, nonce, expiry, signer);

            if (Objects.isNull(module)) {
                throw TransactionCreationException.from(new IllegalArgumentException("Module cannot be null"));
            }
        }

        @Override
        public DeployModuleTransaction build() throws TransactionCreationException {
            val transaction = super.build();
            verifyDeployModuleInput(
                    transaction.sender,
                    transaction.nonce,
                    transaction.expiry,
                    transaction.module,
                    transaction.signer);

            transaction.blockItem = createNewTransaction(transaction).toBlockItem();
            return transaction;
        }

        private Payload createNewTransaction(DeployModuleTransaction transaction) throws TransactionCreationException {
            return DeployModule.createNew(transaction.getModule(), transaction.maxEnergyCost)
                    .withHeader(TransactionHeader.builder()
                            .sender(transaction.getSender())
                            .accountNonce(transaction.getNonce().getNonce())
                            .expiry(transaction.getExpiry().getValue())
                            .build())
                    .signWith(transaction.getSigner());
        }
    }
}
