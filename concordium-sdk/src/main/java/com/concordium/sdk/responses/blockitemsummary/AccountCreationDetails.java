package com.concordium.sdk.responses.blockitemsummary;

import com.concordium.sdk.responses.accountinfo.credential.CredentialType;
import com.concordium.sdk.transactions.CredentialRegistrationId;
import com.concordium.sdk.types.AccountAddress;
import lombok.*;

/**
 * An account was created on the chain.
 */
@ToString
@Getter
@EqualsAndHashCode
@Builder
public class AccountCreationDetails {

    private final AccountAddress address;
    private final CredentialRegistrationId regId;
    private final CredentialType credentialType;

    public static AccountCreationDetails from(com.concordium.grpc.v2.AccountCreationDetails creation) {
        val builder = AccountCreationDetails
                .builder()
                .address(AccountAddress.from(creation.getAddress()))
                .regId(CredentialRegistrationId.from(creation.getRegId()));

        switch (creation.getCredentialType()) {
            case CREDENTIAL_TYPE_INITIAL:
                builder.credentialType(CredentialType.INITIAL);
                break;
            case CREDENTIAL_TYPE_NORMAL:
                builder.credentialType(CredentialType.NORMAL);
                break;
            case UNRECOGNIZED:
                throw new IllegalArgumentException("Unrecognized credential type.");
        }

        return builder.build();

    }
}
