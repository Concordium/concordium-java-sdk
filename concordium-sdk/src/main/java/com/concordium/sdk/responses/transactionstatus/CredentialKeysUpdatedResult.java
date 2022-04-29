package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.CredentialRegistrationId;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
public final class CredentialKeysUpdatedResult extends TransactionResultEvent {
    private CredentialRegistrationId credId;

    @JsonCreator
    CredentialKeysUpdatedResult(@JsonProperty("credId") String credId) {
        if(!Objects.isNull(credId)) {
            this.credId = CredentialRegistrationId.from(credId);
        }
    }
}
