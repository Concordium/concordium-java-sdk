package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.Getter;
import lombok.val;

import java.util.Objects;

@Getter
public class UpdateCredentialKeysTransaction extends AbstractTransaction {

    private final CredentialRegistrationId credentialRegistrationID;
    private final CredentialPublicKeys keys;
    private final AccountAddress sender;
    private final AccountNonce nonce;
    private final Expiry expiry;
    private final TransactionSigner signer;
    private BlockItem blockItem;
    private final UInt64 maxEnergyCost;

    @Builder
    public UpdateCredentialKeysTransaction(CredentialRegistrationId credentialRegistrationID,
                                           CredentialPublicKeys keys,
                                           AccountAddress sender,
                                           AccountNonce nonce,
                                           Expiry expiry,
                                           TransactionSigner signer,
                                           BlockItem blockItem,
                                           UInt64 maxEnergyCost) throws TransactionCreationException {
        this.credentialRegistrationID = credentialRegistrationID;
        this.keys = keys;
        this.sender = sender;
        this.nonce = nonce;
        this.expiry = expiry;
        this.signer = signer;
        this.blockItem = blockItem;
        this.maxEnergyCost = maxEnergyCost;
    }

    public static UpdateCredentialKeysTransactionBuilder builder() {
        return new CustomBuilder();
    }

    @Override
    public BlockItem getBlockItem() {
        return blockItem;
    }

    private static class CustomBuilder extends UpdateCredentialKeysTransactionBuilder {
        @Override
        public UpdateCredentialKeysTransaction build() throws TransactionCreationException {
            val transaction = super.build();
            verifyInitContractInput(
                    transaction.sender,
                    transaction.nonce,
                    transaction.expiry,
                    transaction.signer,
                    transaction.credentialRegistrationID,
                    transaction.keys
            );
            transaction.blockItem = updateCredentialKeysInstance(transaction).toBlockItem();

            return transaction;
        }


        private Payload updateCredentialKeysInstance(UpdateCredentialKeysTransaction transaction) throws TransactionCreationException {
            return UpdateCredentialKeys.createNew(
                            transaction.credentialRegistrationID,
                            transaction.keys,
                            transaction.maxEnergyCost)
                    .withHeader(TransactionHeader.builder()
                            .sender(transaction.sender)
                            .accountNonce(transaction.nonce.getNonce())
                            .expiry(transaction.expiry.getValue())
                            .build())
                    .signWith(transaction.signer);
        }

        static void verifyInitContractInput(AccountAddress sender, AccountNonce nonce, Expiry expiry, TransactionSigner signer, CredentialRegistrationId credentialRegistrationID, CredentialPublicKeys keys) throws TransactionCreationException {
            Transaction.verifyCommonInput(sender, nonce, expiry, signer);
            if (Objects.isNull(credentialRegistrationID)) {
                throw TransactionCreationException.from(new IllegalArgumentException("credentialRegistrationID cannot be null"));
            }
            if (Objects.isNull(keys)) {
                throw TransactionCreationException.from(new IllegalArgumentException("keys cannot be null"));
            }
        }

    }
}
