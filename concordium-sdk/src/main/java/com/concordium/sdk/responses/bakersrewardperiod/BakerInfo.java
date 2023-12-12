package com.concordium.sdk.responses.bakersrewardperiod;

import com.concordium.sdk.crypto.bls.BLSPublicKey;
import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.concordium.sdk.crypto.vrf.VRFPublicKey;
import com.concordium.sdk.responses.BakerId;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Information about a baker.
 */
@Builder
@Getter
@EqualsAndHashCode
@ToString
public class BakerInfo {

    /**
     * Id of the baker.
     */
    private BakerId bakerId;
    /**
     * Baker's public key used to check whether they won the lottery or not.
     */
    private VRFPublicKey bakerElectionVerifyKey;
    /**
     * Baker's public key used to check that they are indeed the ones who produced the block.
     */
    private ED25519PublicKey bakerSignatureVerifyKey;
    /**
     * Baker's public key used to check signatures on finalization records.
     * This is only used if the baker has sufficient stake to participate in finalization.
     */
    private BLSPublicKey bakerAggregationVerifyKey;

    /**
     * Parses {@link com.concordium.grpc.v2.BakerInfo} to {@link BakerInfo}.
     *
     * @param bakerInfo {@link com.concordium.grpc.v2.BakerInfo} returned by the GRPC v2 API.
     * @return Parsed {@link BakerRewardPeriodInfo}.
     */
    public static BakerInfo from(com.concordium.grpc.v2.BakerInfo bakerInfo) {
        return BakerInfo.builder()
                .bakerId(BakerId.from(bakerInfo.getBakerId()))
                .bakerElectionVerifyKey(VRFPublicKey.from(bakerInfo.getElectionKey().getValue().toByteArray()))
                .bakerSignatureVerifyKey(ED25519PublicKey.from(bakerInfo.getSignatureKey().getValue().toByteArray()))
                .bakerAggregationVerifyKey(BLSPublicKey.from(bakerInfo.getAggregationKey().getValue().toByteArray()))
                .build();
    }
}


