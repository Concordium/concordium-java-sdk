package com.concordium.sdk.requests;

import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.transactions.CredentialRegistrationId;
import com.concordium.sdk.types.AccountAddress;
import com.google.protobuf.ByteString;
import lombok.Getter;

import java.nio.charset.StandardCharsets;

/**
 * The account request to use when querying accounts on chain.
 */
@Getter
public final class AccountQuery {

    private final Type type;
    private final AccountAddress address;
    private final AccountIndex index;
    private final CredentialRegistrationId registrationId;

    private AccountQuery(Type type, AccountAddress address, AccountIndex index, CredentialRegistrationId credentialRegistrationId) {
        this.type = type;
        this.address = address;
        this.index = index;
        this.registrationId = credentialRegistrationId;
    }

    public ByteString getByteString() {
        switch (type) {
            case ADDRESS:
                return ByteString.copyFrom(address.getEncodedBytes());
            case INDEX:
                return ByteString.copyFrom(Long.toString(index.getIndex().getValue()).getBytes(StandardCharsets.UTF_8));
            case CREDENTIAL_REGISTRATION_ID:
                return ByteString.copyFromUtf8(registrationId.getEncoded());
            default:
                throw new IllegalStateException("Invalid AccountRequest Type " + type);
        }
    }

    /**
     * Create an {@link AccountQuery} given the provided {@link AccountAddress}
     *
     * @param address The account address
     * @return the AccountRequest
     */
    public static AccountQuery from(AccountAddress address) {
        return new AccountQuery(Type.ADDRESS, address, null, null);
    }

    /**
     * Create an {@link AccountQuery} given the provided {@link AccountIndex}
     *
     * @param index the account index, baker id, delegator id etc.
     * @return the AccountRequest
     */
    public static AccountQuery from(AccountIndex index) {
        return new AccountQuery(Type.INDEX, null, index, null);
    }

    /**
     * Create an {@link AccountQuery} given the provided {@link CredentialRegistrationId}
     *
     * @param credentialRegistrationId the credential registration id.
     * @return the AccountRequest
     */
    public static AccountQuery from(CredentialRegistrationId credentialRegistrationId) {
        return new AccountQuery(Type.CREDENTIAL_REGISTRATION_ID, null, null, credentialRegistrationId);
    }

    @Override
    public String toString() {
        switch (type) {
            case ADDRESS:
                return address.encoded();
            case INDEX:
                return index.toString();
            case CREDENTIAL_REGISTRATION_ID:
                return registrationId.toString();
            default:
                throw new IllegalStateException("Invalid AccountRequest Type " + type);
        }
    }

    public enum Type {
        ADDRESS, INDEX, CREDENTIAL_REGISTRATION_ID
    }
}
