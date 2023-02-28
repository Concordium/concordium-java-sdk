package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.UInt16;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

/**
 * Construct a transaction to update signing keys of a specific credential.
 */
@Getter
public class UpdateCredentialKeysTransaction extends AbstractTransaction {
    /**
     * Id of the credential whose keys are to be updated.
     */
    private final CredentialRegistrationId credentialRegistrationID;
    /**
     *  The new public keys
     */
    private final CredentialPublicKeys keys;

    /**
     * the number of existing credentials on the account.
     * This will affect the estimated transaction cost.
     * It is safe to over-approximate this.
     */
    private final UInt16 numExistingCredentials;

    @Builder
    public UpdateCredentialKeysTransaction(
            final CredentialRegistrationId credentialRegistrationID,
            final CredentialPublicKeys keys,
            final UInt16 numExistingCredentials,
            final AccountAddress sender,
            final AccountNonce nonce,
            final Expiry expiry,
            final TransactionSigner signer) {
        super(sender, nonce, expiry, signer);

        if (Objects.isNull(credentialRegistrationID)) {
            throw TransactionCreationException.from(new IllegalArgumentException("credentialRegistrationID cannot be null"));
        }
        if (Objects.isNull(keys)) {
            throw TransactionCreationException.from(new IllegalArgumentException("keys cannot be null"));
        }
        if (Objects.isNull(numExistingCredentials)) {
            throw TransactionCreationException.from(new IllegalArgumentException("Existing number of credentials cannot be null"));
        }
        this.credentialRegistrationID = credentialRegistrationID;
        this.keys = keys;
        this.numExistingCredentials = numExistingCredentials;
    }

    @Override
    public BlockItem getBlockItem() {
        return UpdateCredentialKeys.createNew(
                        getCredentialRegistrationID(),
                        getKeys(),
                        getNumExistingCredentials())
                .withHeader(TransactionHeader.builder()
                        .sender(getSender())
                        .accountNonce(getNonce().getNonce())
                        .expiry(getExpiry().getValue())
                        .build())
                .signWith(getSigner())
                .toBlockItem();
    }
}
