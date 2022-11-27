package com.concordium.sdk.transactions;

import com.concordium.sdk.exceptions.TransactionCreationException;
import lombok.Builder;
import lombok.val;

import java.util.Objects;

public class ConfigureBakerTransaction extends AbstractTransaction {
    /**
     * Whether to add earnings to the stake automatically or not.
     */
    private final ConfigureBakerPayload payload;

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

    private BlockItem blockItem;

    /**
     * A constructor of {@link ConfigureBakerTransaction} class.
     */
    @Builder
    public ConfigureBakerTransaction(
            ConfigureBakerPayload payload,
            AccountAddress sender,
            AccountNonce nonce,
            Expiry expiry,
            TransactionSigner signer,
            BlockItem blockItem) {
        this.payload = payload;
        this.sender = sender;
        this.nonce = nonce;
        this.expiry = expiry;
        this.signer = signer;
        this.blockItem = blockItem;
    }

    /**
     * @return A new instance of the {@link ConfigureBakerTransaction}  class.
     */
    public static ConfigureBakerTransaction.ConfigureBakerTransactionBuilder builder() {
        return new ConfigureBakerTransaction.CustomBuilder();
    }

    @Override
    public BlockItem getBlockItem() {
        return blockItem;
    }

    private static class CustomBuilder extends ConfigureBakerTransaction.ConfigureBakerTransactionBuilder {
        @Override
        public ConfigureBakerTransaction build() {
            val transaction = super.build();

            try {
                verifyConfigureBakerInput(
                        transaction.sender,
                        transaction.nonce,
                        transaction.expiry,
                        transaction.signer,
                        transaction.payload);
                transaction.blockItem = ConfigureBakerInstance(transaction).toBlockItem();
            } catch (TransactionCreationException e) {
                throw new RuntimeException(e);
            }

            return transaction;
        }

        private Payload ConfigureBakerInstance(ConfigureBakerTransaction transaction) throws TransactionCreationException {
            return ConfigureBaker.createNew(transaction.payload).
                    withHeader(TransactionHeader.builder()
                            .sender(transaction.sender)
                            .accountNonce(transaction.nonce.getNonce())
                            .expiry(transaction.expiry.getValue())
                            .build())
                    .signWith(transaction.signer);
        }

        static void verifyConfigureBakerInput(
                AccountAddress sender,
                AccountNonce nonce,
                Expiry expiry,
                TransactionSigner signer,
                ConfigureBakerPayload payload) throws TransactionCreationException {

            Transaction.verifyAccountTransactionHeaders(sender, nonce, expiry, signer);

            if (Objects.isNull(payload)) {
                throw TransactionCreationException.from(new IllegalArgumentException("Payload cannot be null"));
            }
        }
    }

}
