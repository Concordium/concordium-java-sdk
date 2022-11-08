package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.Getter;
import lombok.val;

import java.util.Objects;


/**
 * Construct a transaction to initialise a smart contract.
 */
@Getter
public class InitContractTransaction extends AbstractTransaction {

    /**
     * Payload to initialize a smart contract.
     */
    private final InitContractPayload payload;

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

    /**
     * A constructor of {@link InitContractTransaction} class.
     */
    @Builder
    public InitContractTransaction(InitContractPayload payload,
                                   AccountAddress sender,
                                   AccountNonce nonce,
                                   Expiry expiry,
                                   TransactionSigner signer,
                                   UInt64 maxEnergyCost) throws TransactionCreationException {
        this.payload = payload;
        this.sender = sender;
        this.nonce = nonce;
        this.expiry = expiry;
        this.signer = signer;
        this.maxEnergyCost = maxEnergyCost;
    }

    /**
     * @return A new instance of the {@link InitContractTransaction}  class.
     */
    public static InitContractTransactionBuilder builder() {
        return new CustomBuilder();
    }

    @Override
    public BlockItem getBlockItem() {
        return blockItem;
    }

    private static class CustomBuilder extends InitContractTransactionBuilder {
        static void verifyInitContractInput(AccountAddress sender, AccountNonce nonce, Expiry expiry, TransactionSigner signer, InitContractPayload payload) throws TransactionCreationException {
            Transaction.verifyAccountTransactionHeaders(sender, nonce, expiry, signer);
            if (Objects.isNull(payload)) {
                throw TransactionCreationException.from(new IllegalArgumentException("Payload cannot be null"));
            }
        }

        // Overriding the build method of the super class.
        @Override
        public InitContractTransaction build() throws TransactionCreationException {
            val transaction = super.build();
            verifyInitContractInput(
                    transaction.sender,
                    transaction.nonce,
                    transaction.expiry,
                    transaction.signer,
                    transaction.payload
            );
            transaction.blockItem = initializeSmartContractInstance(transaction).toBlockItem();

            return transaction;
        }

        private Payload initializeSmartContractInstance(InitContractTransaction transaction) throws TransactionCreationException {
            return InitContract.createNew(
                            transaction.payload,
                            transaction.maxEnergyCost).
                    withHeader(TransactionHeader.builder()
                            .sender(transaction.sender)
                            .accountNonce(transaction.nonce.getNonce())
                            .expiry(transaction.expiry.getValue())
                            .build())
                    .signWith(transaction.signer);
        }

    }
}
