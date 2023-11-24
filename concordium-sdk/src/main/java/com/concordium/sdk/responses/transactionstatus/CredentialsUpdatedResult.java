package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.AccountTransactionEffects;
import com.concordium.sdk.transactions.CredentialRegistrationId;
import com.concordium.sdk.types.AccountAddress;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The credentials have been updated.
 */
@Getter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public final class CredentialsUpdatedResult implements TransactionResultEvent {

    /**
     * The account which credentials have been updated.
     */
    private final AccountAddress account;

    /**
     * The new {@link CredentialRegistrationId}`s.
     */
    private final List<CredentialRegistrationId> newCredIds;

    /**
     * The removed {@link CredentialRegistrationId}`s.
     */
    private final List<CredentialRegistrationId> removedCredIds;

    /**
     * The new account threshold
     */
    private final int newThreshold;

    public static CredentialsUpdatedResult from(AccountTransactionEffects.CredentialsUpdated credentialsUpdated, AccountAddress account) {
        return CredentialsUpdatedResult
                .builder()
                .account(account)
                .newCredIds(credentialsUpdated.getNewCredIdsList().stream().map(CredentialRegistrationId::from).collect(Collectors.toList()))
                .removedCredIds(credentialsUpdated.getRemovedCredIdsList().stream().map(CredentialRegistrationId::from).collect(Collectors.toList()))
                .newThreshold(credentialsUpdated.getNewThreshold().getValue())
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.CREDENTIALS_UPDATED;
    }
}
