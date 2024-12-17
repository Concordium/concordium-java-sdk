package com.concordium.sdk.responses.consensusstatus;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@EqualsAndHashCode
public class RoundTimeout {

    /**
     * Timeout certificate for the round that timed out.
     */
    private final RawTimeoutCertificate timeoutCertificate;

    /**
     * The highest known quorum certificate when the round timed out.
     */
    private final RawQuorumCertificate quorumCertificate;
}
