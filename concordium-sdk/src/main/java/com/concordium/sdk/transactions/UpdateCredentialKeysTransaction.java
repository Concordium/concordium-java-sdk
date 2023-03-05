package com.concordium.sdk.transactions;


import com.concordium.sdk.types.UInt16;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * Construct a transaction to update signing keys of a specific credential.
 */
@Getter
public class UpdateCredentialKeysTransaction extends AbstractTransaction {
    @Builder
    public UpdateCredentialKeysTransaction(
            @NonNull final CredentialRegistrationId credentialRegistrationID,
            @NonNull final CredentialPublicKeys keys,
            @NonNull final UInt16 numExistingCredentials,
            @NonNull final AccountAddress sender,
            @NonNull final AccountNonce nonce,
            @NonNull final Expiry expiry,
            @NonNull final TransactionSigner signer) {
        super(sender, nonce, expiry, signer, UpdateCredentialKeys.createNew(
                credentialRegistrationID,
                keys,
                numExistingCredentials));
    }
}
