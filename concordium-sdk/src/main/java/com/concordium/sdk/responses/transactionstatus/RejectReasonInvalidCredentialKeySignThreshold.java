package com.concordium.sdk.responses.transactionstatus;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * When the credential key threshold is updated, it must not exceed the amount of existing keys.
 */
@ToString
@EqualsAndHashCode
public class RejectReasonInvalidCredentialKeySignThreshold extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_CREDENTIAL_KEY_SIGN_THRESHOLD;
    }
}
