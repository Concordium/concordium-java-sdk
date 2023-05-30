package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.CredentialRegistrationId;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import lombok.*;

import java.util.List;
import java.util.Objects;

/**
 * Some of the credential IDs already exist or are duplicated in the transaction.
 */
@Getter
@Builder
@ToString
@EqualsAndHashCode
public class RejectReasonDuplicateCredIDs extends RejectReason {
    private List<CredentialRegistrationId> duplicates;

    @JsonCreator
    RejectReasonDuplicateCredIDs(@JsonProperty("contents") List<CredentialRegistrationId> duplicates) {
        if(!Objects.isNull(duplicates)) {
            this.duplicates = duplicates;
        }
    }

    /**
     * Parses {@link com.concordium.grpc.v2.RejectReason.DuplicateCredIds} to {@link RejectReasonDuplicateCredIDs}.
     * @param duplicateCredIds {@link com.concordium.grpc.v2.RejectReason.DuplicateCredIds} returned by the GRPC V2 API.
     * @return parsed {@link RejectReasonDuplicateCredIDs}.
     */
    public static RejectReasonDuplicateCredIDs parse(com.concordium.grpc.v2.RejectReason.DuplicateCredIds duplicateCredIds) {
        val list = new ImmutableList.Builder<CredentialRegistrationId>();
        duplicateCredIds.getIdsList().forEach(id -> list.add(CredentialRegistrationId.fromBytes(id.getValue().toByteArray())));
        return RejectReasonDuplicateCredIDs.builder().duplicates(list.build()).build();
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.DUPLICATE_CRED_IDS;
    }
}
