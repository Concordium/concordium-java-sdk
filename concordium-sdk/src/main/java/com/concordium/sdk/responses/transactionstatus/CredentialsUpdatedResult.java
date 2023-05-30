package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.AccountTransactionEffects;
import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.CredentialRegistrationId;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The credentials have been updated.
 * This is the result of a successful UpdateCredentials transaction.
 */
@Getter
@ToString
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public final class CredentialsUpdatedResult implements TransactionResultEvent {

    /**
     * The account which credentials has been updated.
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

    @JsonCreator
    CredentialsUpdatedResult(@JsonProperty("account") AccountAddress account,
                             @JsonProperty("newCredIds") List<String> newCredIds,
                             @JsonProperty("removedCredIds") List<String> removedCredIds,
                             @JsonProperty("newThreshold") String newThreshold) {
        this.account = account;
        this.newCredIds = new ArrayList<>();
        for (String newCredId : newCredIds) {
            this.newCredIds.add(CredentialRegistrationId.from(newCredId));
        }
        this.removedCredIds = new ArrayList<>();
        for (String removedCredId : removedCredIds) {
            this.removedCredIds.add(CredentialRegistrationId.from(removedCredId));
        }
        this.newThreshold = Integer.parseInt(newThreshold);
    }

    /**
     * Parses {@link AccountTransactionEffects.CredentialsUpdated} and {@link com.concordium.grpc.v2.AccountAddress} to {@link CredentialsUpdatedResult}.
     * @param credentialsUpdated {@link AccountTransactionEffects.CredentialsUpdated} returned by the GRPC V2 API.
     * @param sender {@link com.concordium.grpc.v2.AccountAddress} returned by the GRPC V2 API.
     * @return parsed {@link CredentialsUpdatedResult}.
     */
    public static CredentialsUpdatedResult parse(AccountTransactionEffects.CredentialsUpdated credentialsUpdated, com.concordium.grpc.v2.AccountAddress sender) {
        val newCreds = new ImmutableList.Builder<CredentialRegistrationId>();
        val removedCreds = new ImmutableList.Builder<CredentialRegistrationId>();
        credentialsUpdated.getNewCredIdsList().forEach(c -> newCreds.add(CredentialRegistrationId.fromBytes(c.getValue().toByteArray())));
        credentialsUpdated.getRemovedCredIdsList().forEach(c -> removedCreds.add(CredentialRegistrationId.fromBytes(c.getValue().toByteArray())));
        return CredentialsUpdatedResult.builder()
                .account(AccountAddress.parse(sender))
                .newThreshold(credentialsUpdated.getNewThreshold().getValue())
                .newCredIds(newCreds.build())
                .removedCredIds(removedCreds.build())
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.CREDENTIALS_UPDATED;
    }
}
