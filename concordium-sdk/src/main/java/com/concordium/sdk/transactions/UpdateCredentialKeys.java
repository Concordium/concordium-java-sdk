package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt16;
import com.concordium.sdk.types.UInt64;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

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

    public UpdateCredentialKeys(CredentialRegistrationId credentialRegistrationID, CredentialPublicKeys keys, UInt16 numExistingCredentials) {
        this.credentialRegistrationID = credentialRegistrationID;
        this.keys = keys;
        this.numExistingCredentials = numExistingCredentials;
    }

    /**
     * This function returns the type of the payload.
     */
    @Override
    public PayloadType getType() {
        return PayloadType.UPDATE_CREDENTIAL_KEYS;
    }

    @Override
    UInt64 getTransactionTypeCost() {
        val numCredKeys = keys.getKeys().size();
        val maxEnergyCost = UInt64.from(500 * numExistingCredentials.getValue() + 100 * numCredKeys);
        return maxEnergyCost;
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.UPDATE_CREDENTIAL_KEYS;
    }

    @Override
    public byte[] getTransactionPayloadBytes() {
        val credentialRegistrationIdBytes = credentialRegistrationID.getRegId();
        val keysBytes = keys.getBytes();
        val buffer = ByteBuffer.allocate(credentialRegistrationIdBytes.length + keysBytes.length);
        buffer.put(credentialRegistrationIdBytes);
        buffer.put(keysBytes);

        return buffer.array();
    }

    static UpdateCredentialKeys createNew(CredentialRegistrationId credentialRegistrationID, CredentialPublicKeys keys, UInt16 numExistingCredentials) {
        return new UpdateCredentialKeys(credentialRegistrationID, keys, numExistingCredentials);
    }
}
