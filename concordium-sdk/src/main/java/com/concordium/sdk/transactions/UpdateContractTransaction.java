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
     * The payload for updating a smart contract.
     */
    private final UpdateContractPayload payload;

    /**
     * Account Address of the sender.
     */
    private final AccountAddress sender;
    /**
     * The senders account next available nonce.
     */
    private final AccountNonce nonce;
    /**
     * Indicates when the transaction should expire.
     */
    private final Expiry expiry;
    /**
     * A signer object that is used to sign the transaction.
     */
    private final TransactionSigner signer;

    /**
     * Maximum energy **allowed** for the transaction to use.
     */
    private final UInt64 maxEnergyCost;
    private BlockItem blockItem;

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
