package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.UInt16;
import lombok.*;

/**
 * Construct a transaction to update signing keys of a specific credential.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UpdateCredentialKeysTransaction extends AccountTransaction {
    private UpdateCredentialKeysTransaction(
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

    /**
     * Creates a new instance of {@link UpdateCredentialKeysTransaction}.
     *
     * @param credentialRegistrationID {@link CredentialRegistrationId}.
     * @param keys                     Public keys of the credentials.
     * @param numExistingCredentials   No of existing Credentials.
     * @param sender                   Sender ({@link AccountAddress}) of this Transaction.
     * @param nonce                    Account {@link com.concordium.sdk.types.Nonce} Of the Sender Account.
     * @param expiry                   {@link Expiry} of this transaction.
     * @param signer                   {@link Signer} of this transaction.
     * @throws TransactionCreationException On failure to create the Transaction from input params.
     *                                      Ex when any of the input param is NULL.
     */
    @Builder
    public static UpdateCredentialKeysTransaction from(
            final CredentialRegistrationId credentialRegistrationID,
            final CredentialPublicKeys keys,
            final UInt16 numExistingCredentials,
            final AccountAddress sender,
            final AccountNonce nonce,
            final Expiry expiry,
            final TransactionSigner signer) {
        try {
            return new UpdateCredentialKeysTransaction(credentialRegistrationID, keys, numExistingCredentials, sender, nonce, expiry, signer);
        } catch (NullPointerException nullPointerException) {
            throw TransactionCreationException.from(nullPointerException);
        }
    }
}
