package com.concordium.sdk.responses.transactionevent;

import com.concordium.sdk.responses.accountinfo.credential.CredentialType;
import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.CredentialRegistrationId;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Details about an account creation.
 */
@Builder
@Getter
@EqualsAndHashCode
@ToString
public class AccountCreationDetails {

    /**
     * Whether this is an initial or normal account.
     */
    private CredentialType credentialType;

    /**
     * Address of the newly created account.
     */
    private AccountAddress address;

    /**
     * Credential registration ID of the first credential.
     */
    private CredentialRegistrationId registrationId;

    /**
     * Parses {@link com.concordium.grpc.v2.AccountCreationDetails} to {@link AccountCreationDetails}
     * @param accountCreationDetails {@link com.concordium.grpc.v2.AccountCreationDetails} returned from the GRPC V2 API.
     * @return parsed {@link AccountCreationDetails}
     */
    public static AccountCreationDetails parse(com.concordium.grpc.v2.AccountCreationDetails accountCreationDetails) {
        return AccountCreationDetails.builder()
                .credentialType(CredentialType.parse(accountCreationDetails.getCredentialType()))
                .address(AccountAddress.parse(accountCreationDetails.getAddress()))
                .registrationId(CredentialRegistrationId.fromBytes(accountCreationDetails.getRegId().getValue().toByteArray()))
                .build();
    }
}
