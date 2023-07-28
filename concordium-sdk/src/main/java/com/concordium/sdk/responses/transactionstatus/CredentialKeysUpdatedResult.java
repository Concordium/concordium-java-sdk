package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.CredentialRegistrationId;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public final class CredentialKeysUpdatedResult implements TransactionResultEvent {
    private final CredentialRegistrationId credId;

    @JsonCreator
    CredentialKeysUpdatedResult(@JsonProperty("credId") String credId) {
        this.credId = CredentialRegistrationId.from(credId);

    }

    public static CredentialKeysUpdatedResult from(com.concordium.grpc.v2.CredentialRegistrationId credentialKeysUpdated) {
        return CredentialKeysUpdatedResult
                .builder()
                .credId(CredentialRegistrationId.from(credentialKeysUpdated))
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.CREDENTIAL_KEYS_UPDATED;
    }
}
