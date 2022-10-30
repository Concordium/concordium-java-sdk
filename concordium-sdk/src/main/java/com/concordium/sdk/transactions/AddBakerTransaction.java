package com.concordium.sdk.transactions;

import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.val;

import java.util.Objects;

public class AddBakerTransaction extends AbstractTransaction {
    /**
     * Initial baking stake.
     */
    private final CCDAmount bakingState;
    /**
     * Whether to add earnings to the stake automatically or not.
     */
    private final boolean restakeEarnings;

    private final AccountAddress sender;
    private final AccountNonce nonce;
    private final Expiry expiry;
    private final TransactionSigner signer;
    private BlockItem blockItem;
    private final UInt64 maxEnergyCost;

    @Builder
    public AddBakerTransaction(
            CCDAmount bakingState,
            boolean restakeEarnings,
            AccountAddress sender,
            AccountNonce nonce,
            Expiry expiry,
            TransactionSigner signer,
            BlockItem blockItem,
            UInt64 maxEnergyCost) {
        this.bakingState = bakingState;
        this.restakeEarnings = restakeEarnings;
        this.sender = sender;
        this.nonce = nonce;
        this.expiry = expiry;
        this.signer = signer;
        this.blockItem = blockItem;
        this.maxEnergyCost = maxEnergyCost;
    }

    public static AddBakerTransaction.AddBakerTransactionBuilder builder() {
        return new AddBakerTransaction.CustomBuilder();
    }

    @Override
    public BlockItem getBlockItem() {
        return blockItem;
    }

    private static class CustomBuilder extends AddBakerTransaction.AddBakerTransactionBuilder {
        @Override
        public AddBakerTransaction build() {
            val transaction = super.build();

            try {
                verifyAddBakerInput(
                        transaction.sender,
                        transaction.nonce,
                        transaction.expiry,
                        transaction.signer,
                        transaction.bakingState,
                        transaction.restakeEarnings);
                transaction.blockItem = AddBakerInstance(transaction).toBlockItem();
            } catch (TransactionCreationException e) {
                throw new RuntimeException(e);
            }

            return transaction;
        }

        private Payload AddBakerInstance(AddBakerTransaction transaction) throws TransactionCreationException {
            return AddBaker.createNew(
                            transaction.sender,
                            transaction.bakingState,
                            transaction.restakeEarnings,
                            transaction.maxEnergyCost).
                    withHeader(TransactionHeader.builder()
                            .sender(transaction.sender)
                            .accountNonce(transaction.nonce.getNonce())
                            .expiry(transaction.expiry.getValue())
                            .build())
                    .signWith(transaction.signer);
        }

        static void verifyAddBakerInput(
                AccountAddress sender,
                AccountNonce nonce,
                Expiry expiry,
                TransactionSigner signer,
                CCDAmount bakingState,
                boolean restakeEarnings) throws TransactionCreationException {

            Transaction.verifyTransactionInput(sender, nonce, expiry, signer);

            if (Objects.isNull(bakingState)) {
                throw TransactionCreationException.from(new IllegalArgumentException("Baking State cannot be null"));
            }
            if (Objects.isNull(restakeEarnings)) {
                throw TransactionCreationException.from(new IllegalArgumentException("Restake Earnings cannot be null"));
            }
        }
    }

}
