package com.concordium.sdk.transactions;

import com.concordium.sdk.exceptions.TransactionCreationException;
import lombok.Builder;
import lombok.val;

import java.util.Objects;

/**
 * A class that represents a ConfigureDelegationTransaction.
 * This transaction is used to register the account as a delegator.
 */
public class ConfigureDelegationTransaction extends AbstractTransaction {
    /**
     * Whether to add earnings to the stake automatically or not.
     */
    private final ConfigureDelegationPayload payload;
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

    @Builder
    public ConfigureDelegationTransaction(
            ConfigureDelegationPayload payload,
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

    public static ConfigureDelegationTransaction.ConfigureDelegationTransactionBuilder builder() {
        return new ConfigureDelegationTransaction.CustomBuilder();
    }

    @Override
    public BlockItem getBlockItem() {
        return blockItem;
    }

    private static class CustomBuilder extends ConfigureDelegationTransaction.ConfigureDelegationTransactionBuilder {
        @Override
        public ConfigureDelegationTransaction build() {
            val transaction = super.build();

            try {
                verifyConfigureDelegationInput(
                        transaction.sender,
                        transaction.nonce,
                        transaction.expiry,
                        transaction.signer,
                        transaction.payload);
                transaction.blockItem = ConfigureDelegationInstance(transaction).toBlockItem();
            } catch (TransactionCreationException e) {
                throw new RuntimeException(e);
            }

            return transaction;
        }

        private Payload ConfigureDelegationInstance(ConfigureDelegationTransaction transaction) throws TransactionCreationException {
            return ConfigureDelegation.createNew(transaction.payload).
                    withHeader(TransactionHeader.builder()
                            .sender(transaction.sender)
                            .accountNonce(transaction.nonce.getNonce())
                            .expiry(transaction.expiry.getValue())
                            .build())
                    .signWith(transaction.signer);
        }

        static void verifyConfigureDelegationInput(
                AccountAddress sender,
                AccountNonce nonce,
                Expiry expiry,
                TransactionSigner signer,
                ConfigureDelegationPayload payload) throws TransactionCreationException {

            Transaction.verifyAccountTransactionHeaders(sender, nonce, expiry, signer);

            if (Objects.isNull(payload)) {
                throw TransactionCreationException.from(new IllegalArgumentException("Payload cannot be null"));
            }
        }
    }

}
