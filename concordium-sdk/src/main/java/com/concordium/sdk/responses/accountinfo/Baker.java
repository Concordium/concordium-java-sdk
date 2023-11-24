package com.concordium.sdk.responses.accountinfo;

import com.concordium.sdk.crypto.bls.BLSPublicKey;
import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.concordium.sdk.crypto.elgamal.ElgamalPublicKey;
import com.concordium.sdk.crypto.pointchevalsanders.PSPublicKey;
import com.concordium.sdk.crypto.vrf.VRFPublicKey;
import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.responses.bakersrewardperiod.BakerInfo;
import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@ToString
@EqualsAndHashCode
@Jacksonized
@Builder
public final class Baker {

    /**
     * The staked amount.
     */
    @Getter
    private final CCDAmount stakedAmount;

    /**
     * Whether earnings should be restaked automatically or not.
     */
    @Getter
    private final boolean restakeEarnings;

    /**
     * Information about the baker that is staking.
     */
    @Getter
    private final BakerInfo bakerInfo;

    /**
     * The pending changes for the baker.
     */
    @Getter
    private final PendingChange pendingChange;

    /**
     * The baker pool info
     */
    @Getter
    private final BakerPoolInfo bakerPoolInfo;

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
