package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.CredentialRegistrationId;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * A credential id that was to be removed is not part of the account.
 */
@ToString
@Builder
public class RejectReasonNonExistentCredIDs extends RejectReason {
    @Getter
    private final List<CredentialRegistrationId> ids;

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.NON_EXISTENT_CREDENTIAL_ID;
    }
}
