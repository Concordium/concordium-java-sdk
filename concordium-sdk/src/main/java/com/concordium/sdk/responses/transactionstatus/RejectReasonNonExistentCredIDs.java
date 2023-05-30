package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.CredentialRegistrationId;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import lombok.*;

import java.util.List;

/**
 * A credential id that was to be removed is not part of the account.
 */
@ToString
@Builder
@EqualsAndHashCode
public class RejectReasonNonExistentCredIDs extends RejectReason {
    @Getter
    private final List<CredentialRegistrationId> ids;

    @JsonCreator
    RejectReasonNonExistentCredIDs(@JsonProperty("contents") List<CredentialRegistrationId> ids) {
        this.ids = ids;
    }


    /**
     * Parses {@link com.concordium.grpc.v2.RejectReason.NonExistentCredIds} to {@link RejectReasonNonExistentCredIDs}.
     * @param nonExistentCredIds {@link com.concordium.grpc.v2.RejectReason.NonExistentCredIds} returned by the GRPC V2 API.
     * @return parsed {@link RejectReasonNonExistentCredIDs}.
     */
    public static RejectReasonNonExistentCredIDs parse(com.concordium.grpc.v2.RejectReason.NonExistentCredIds nonExistentCredIds) {
        val list = new ImmutableList.Builder<CredentialRegistrationId>();
        nonExistentCredIds.getIdsList().forEach(id -> list.add(CredentialRegistrationId.fromBytes(id.getValue().toByteArray())));
        return RejectReasonNonExistentCredIDs.builder()
                .ids(list.build())
                .build();
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.NON_EXISTENT_CREDENTIAL_ID;
    }
}
