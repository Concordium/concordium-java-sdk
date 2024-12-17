package com.concordium.sdk.responses.consensusstatus;

import com.concordium.sdk.crypto.bls.BLSPublicKey;
import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.concordium.sdk.crypto.vrf.VRFPublicKey;
import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.transactions.CCDAmount;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * The keys and stake of a specific baker.
 */
@Getter
@Builder
@ToString
@EqualsAndHashCode
public class FullBakerInfo {
    /**
     * Baker's identity.
     */
    private final BakerId bakerIdentity;

    /**
     * Baker's public key used to check whether they won the lottery or not.
     */
    private final VRFPublicKey electionVerifyKey;

    /**
     * Baker's public key used to check that they are indeed the ones who produced the block.
     */
    private final ED25519PublicKey signatureVerifyKey;

    /**
     * Baker's public key used to check signatures on finalization records.
     * This is only used if the baker has sufficient stake to participate in finalization.
     */
    private final BLSPublicKey aggregationVerifyKey;

    /**
     * The stake of the baker.
     */
    private final CCDAmount stake;
}
