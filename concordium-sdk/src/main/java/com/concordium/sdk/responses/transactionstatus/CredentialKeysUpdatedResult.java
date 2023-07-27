package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.CredentialRegistrationId;
import com.concordium.sdk.types.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@Builder
@EqualsAndHashCode(callSuper = true)
public final class CredentialKeysUpdatedResult extends TransactionResultEvent {
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
