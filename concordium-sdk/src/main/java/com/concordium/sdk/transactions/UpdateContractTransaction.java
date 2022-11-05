package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.Getter;
import lombok.val;

import java.util.Objects;

/**
 * Construct a transaction to update a smart contract instance.
 */
@Getter
public class UpdateContractTransaction extends AbstractTransaction {
    /**
     * Data needed to update a smart contract.
     */
    private final UpdateContractPayload payload;

    private final AccountAddress sender;
    private final AccountNonce nonce;
    private final Expiry expiry;
    private final TransactionSigner signer;
    private BlockItem blockItem;
    private final UInt64 maxEnergyCost;

    @Builder
    public UpdateContractTransaction(UpdateContractPayload payload,
                                     AccountAddress sender,
                                     AccountNonce nonce,
                                     Expiry expiry,
                                     TransactionSigner signer,
                                     BlockItem blockItem,
                                     UInt64 maxEnergyCost) throws TransactionCreationException  {
        this.payload = payload;
        this.sender = sender;
        this.nonce = nonce;
        this.expiry = expiry;
        this.signer = signer;
        this.blockItem = blockItem;
        this.maxEnergyCost = maxEnergyCost;
    }

    /**
     * This function returns a new instance of the UpdateContractTransactionBuilder class.
     */
    public static UpdateContractTransactionBuilder builder() {
        return new CustomBuilder();
    }

    /**
     * This function returns the block item associated with this block.
     */
    @Override
    public BlockItem getBlockItem() {
        return blockItem;
    }

    private static class CustomBuilder extends UpdateContractTransactionBuilder {
        /**
         * > The function verifies the input parameters and then calls the updateSmartContractInstance function to update
         * the smart contract instance
         *
         * @return A new UpdateContractTransaction object.
         */
        @Override
        public UpdateContractTransaction build() throws TransactionCreationException {
            val transaction = super.build();
            verifyUpdateContractInput(transaction.sender, transaction.nonce, transaction.expiry, transaction.signer, transaction.payload);
            transaction.blockItem = updateSmartContractInstance(transaction).toBlockItem();
            return transaction;
        }


        private Payload updateSmartContractInstance(UpdateContractTransaction transaction) throws TransactionCreationException {
            return UpdateContract.createNew(
                            transaction.payload,
                            transaction.maxEnergyCost).
                    withHeader(TransactionHeader.builder()
                            .sender(transaction.sender)
                            .accountNonce(transaction.nonce.getNonce())
                            .expiry(transaction.expiry.getValue())
                            .build())
                    .signWith(transaction.signer);
        }

        static void verifyUpdateContractInput(AccountAddress sender, AccountNonce nonce, Expiry expiry, TransactionSigner signer, UpdateContractPayload payload) throws TransactionCreationException {
            Transaction.verifyAccountTransactionHeaders(sender, nonce, expiry, signer);
            if (Objects.isNull(payload)) {
                throw TransactionCreationException.from(new IllegalArgumentException("Payload cannot be null"));
            }
        }
    }
}
