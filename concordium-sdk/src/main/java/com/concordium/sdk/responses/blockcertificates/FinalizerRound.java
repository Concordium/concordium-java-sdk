package com.concordium.sdk.responses.blockcertificates;

import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.responses.Round;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The finalizer round is a map from a {@link Round} to the list of finalizers (identified by their {@link BakerId})
 * that signed off the round.
 */
@Builder
@Getter
@EqualsAndHashCode
@ToString
public class FinalizerRound {
    /**
     * The round that was signed off.
     */
    private final Round round;
    /**
     * The finalizers (identified by their {@link BakerId}) that signed off the in {@link FinalizerRound#round}.
     */
    private final List<BakerId> finalizers;

    /**
     * Parses {@link com.concordium.grpc.v2.FinalizerRound} to {@link FinalizerRound}.
     * @param finalizerRound {@link com.concordium.grpc.v2.FinalizerRound} returned by the GRPC v2 API.
     * @return parsed {@link FinalizerRound}.
     */
    public static FinalizerRound from(com.concordium.grpc.v2.FinalizerRound finalizerRound) {
        val finalizers = new ArrayList<BakerId>();
        finalizerRound.getFinalizersList().forEach(
                f -> finalizers.add(BakerId.from(f))
        );
        return FinalizerRound.builder()
                .round(Round.from(finalizerRound.getRound()))
                .finalizers(finalizers).build();

    }
}
