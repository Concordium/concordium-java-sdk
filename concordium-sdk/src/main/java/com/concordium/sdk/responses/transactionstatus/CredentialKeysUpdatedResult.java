package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.CredentialRegistrationId;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public final class CredentialKeysUpdatedResult extends TransactionResultEvent {
    private final CredentialRegistrationId credId;

    @JsonCreator
    CredentialKeysUpdatedResult(@JsonProperty("credId") String credId) {
        this.credId = CredentialRegistrationId.from(credId);

    }

    /**
     * Parses {@link com.concordium.grpc.v2.CredentialRegistrationId} to {@link CredentialKeysUpdatedResult}.
     * @param credentialKeysUpdated {@link com.concordium.grpc.v2.CredentialRegistrationId} returned by the GRPC V2 API.
     * @return parsed {@link CredentialKeysUpdatedResult}.
     */
    public static CredentialKeysUpdatedResult parse(com.concordium.grpc.v2.CredentialRegistrationId credentialKeysUpdated) {
        return CredentialKeysUpdatedResult.builder()
                .credId(CredentialRegistrationId.fromBytes(credentialKeysUpdated.getValue().toByteArray()))
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.CREDENTIAL_KEYS_UPDATED;
    }
}
