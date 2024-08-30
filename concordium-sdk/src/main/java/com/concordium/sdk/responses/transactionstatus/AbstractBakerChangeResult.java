package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.concordium.sdk.responses.BakerId;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * An event that is emitted when keys of a baker has changed (or the baker has been registered)
 */
@Getter
@ToString
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractBakerChangeResult extends AbstractBakerResult {
    private final BakerId bakerId;

    /**
     * The public VRF key of the baker.
     */
    private final byte[] electionKey;

    /**
     * The public BLS key of the baker.
     */
    private final byte[] aggregationKey;

    /**
     * The public eddsa key of the baker.
     */
    private final ED25519PublicKey signKey;
}
