package com.concordium.sdk.responses.accountinfo;

import com.concordium.sdk.crypto.bls.BLSPublicKey;
import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.concordium.sdk.crypto.vrf.VRFPublicKey;
import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.responses.bakersrewardperiod.BakerInfo;
import com.concordium.sdk.transactions.CCDAmount;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.util.Optional;

@Getter
@ToString
@EqualsAndHashCode
@Jacksonized
@Builder
public final class Baker {

    /**
     * The staked amount.
     */
    private final CCDAmount stakedAmount;

    /**
     * Whether earnings should be restaked automatically or not.
     */
    private final boolean restakeEarnings;

    /**
     * Information about the baker that is staking.
     */
    private final BakerInfo bakerInfo;

    /**
     * The pending changes for the baker.
     * Only present if there is a pending change for the baker.
     */
    private final Optional<PendingChange> pendingChange;

    /**
     * The baker pool info
     */
    private final BakerPoolInfo bakerPoolInfo;

    /**
     * A flag indicating whether the account is currently suspended or not. The
     * flag has a meaning from protocol version 8 onwards. In protocol version 8
     * it signals whether an account has been suspended and is not participating
     * in the consensus algorithm. For protocol version < 8 the flag will always
     * be set to false.
     */
    private final boolean isSuspended;

    public BakerId getBakerId() {
        return bakerInfo.getBakerId();
    }

    public VRFPublicKey getBakerElectionVerifyKey() {
        return bakerInfo.getBakerElectionVerifyKey();
    }

    public ED25519PublicKey getBakerSignatureVerifyKey() {
        return bakerInfo.getBakerSignatureVerifyKey();
    }

    public BLSPublicKey getBakerAggregationVerifyKey() {
        return bakerInfo.getBakerAggregationVerifyKey();
    }
}
