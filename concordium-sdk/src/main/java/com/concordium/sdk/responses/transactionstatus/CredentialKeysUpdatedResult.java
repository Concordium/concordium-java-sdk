package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.responses.transactionevent.accounttransactionresults.AccountTransactionResult;
import com.concordium.sdk.responses.transactionevent.accounttransactionresults.TransactionType;
import com.concordium.sdk.transactions.CredentialRegistrationId;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * The keys of a specific credential were updated.
 */
@Getter
@ToString
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
public final class CredentialKeysUpdatedResult extends TransactionResultEvent implements AccountTransactionResult {

    /**
     * RegistrationID of the credential.
     */
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

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.UPDATE_CREDENTIAL_KEYS;
    }
}
