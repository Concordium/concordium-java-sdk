package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt16;
import lombok.*;

import java.nio.ByteBuffer;


/**
 * Update signing keys of a specific credential.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public final class UpdateCredentialKeys extends Payload {
    /**
     * Id of the credential whose keys are to be updated.
     */
    private final CredentialRegistrationId credentialRegistrationID;

    /**
     * The new public keys
     */
    private final CredentialPublicKeys keys;

    /**
     * the number of existing credentials on the account.
     * This will affect the estimated transaction cost.
     * It is safe to over-approximate this.
     */
    private final UInt16 numExistingCredentials;

    @Builder
    public UpdateCredentialKeys(@NonNull CredentialRegistrationId credentialRegistrationID,
                                @NonNull CredentialPublicKeys keys,
                                @NonNull UInt16 numExistingCredentials) {
        super(TransactionType.UPDATE_CREDENTIAL_KEYS);
        this.credentialRegistrationID = credentialRegistrationID;
        this.keys = keys;
        this.numExistingCredentials = numExistingCredentials;
    }

    @Override
    protected byte[] getPayloadBytes() {
        val credentialRegistrationIdBytes = credentialRegistrationID.getRegId();
        val keysBytes = keys.getBytes();
        val buffer = ByteBuffer.allocate(credentialRegistrationIdBytes.length + keysBytes.length);
        buffer.put(credentialRegistrationIdBytes);
        buffer.put(keysBytes);

        return buffer.array();
    }
}
