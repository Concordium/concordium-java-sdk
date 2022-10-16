package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt64;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public final class UpdateCredentialKeys extends Payload {

    private final static TransactionType TYPE = TransactionType.UPDATE_CREDENTIAL_KEYS;

    private final CredentialRegistrationId credentialRegistrationID;

    private final CredentialPublicKeys keys;

    private final UInt64 maxEnergyCost;

    public UpdateCredentialKeys(CredentialRegistrationId credentialRegistrationID, CredentialPublicKeys keys, UInt64 maxEnergyCost) {
        this.credentialRegistrationID = credentialRegistrationID;
        this.keys = keys;
        this.maxEnergyCost = maxEnergyCost;
    }

    @Override
    public PayloadType getType() {
        return PayloadType.UPDATE_CREDENTIAL_KEYS;
    }

    @Override
    byte[] getBytes() {
        val credentialRegistrationIdBytes = credentialRegistrationID.getRegId();
        val keysBytes = keys.getBytes();
        val buffer = ByteBuffer.allocate(TransactionType.BYTES + credentialRegistrationIdBytes.length + keysBytes.length);
        buffer.put(TransactionType.UPDATE_CREDENTIAL_KEYS.getValue());
        buffer.put(credentialRegistrationIdBytes);
        buffer.put(keysBytes);
        return buffer.array();
    }
    @Override
    UInt64 getTransactionTypeCost() {
        return this.maxEnergyCost;
    }

    static UpdateCredentialKeys createNew(CredentialRegistrationId credentialRegistrationID, CredentialPublicKeys keys, UInt64 maxEnergyCost) {
        return new UpdateCredentialKeys(credentialRegistrationID, keys, maxEnergyCost);
    }
}
