package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class CredentialKeysUpdatedResult extends TransactionResultEvent {
    private final String credId;

    @JsonCreator
    CredentialKeysUpdatedResult(@JsonProperty("credId") String credId) {
        this.credId = credId;

    }
}
