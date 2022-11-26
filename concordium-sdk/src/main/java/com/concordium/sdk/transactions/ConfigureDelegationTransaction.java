package com.concordium.sdk.transactions;

import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.val;

import java.util.Objects;

public class ConfigureDelegationTransaction extends AbstractTransaction {
    /**
     * Whether to add earnings to the stake automatically or not.
     */
    private final ConfigureDelegationPayload payload;
    private final AccountAddress sender;
    private final AccountNonce nonce;
    private final Expiry expiry;
    private final TransactionSigner signer;
    private BlockItem blockItem;
    private final UInt64 maxEnergyCost;

    @Builder
    public ConfigureDelegationTransaction(
            ConfigureDelegationPayload payload,
            AccountAddress sender,
            AccountNonce nonce,
            Expiry expiry,
            TransactionSigner signer,
            BlockItem blockItem,
            UInt64 maxEnergyCost) {
        this.payload = payload;
        this.sender = sender;
        this.nonce = nonce;
        this.expiry = expiry;
        this.signer = signer;
        this.blockItem = blockItem;
        this.maxEnergyCost = maxEnergyCost;
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
            return ConfigureDelegation.createNew(
                            transaction.payload,
                            transaction.maxEnergyCost).
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
