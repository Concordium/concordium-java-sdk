package com.concordium.sdk.responses.blockcertificates;

import com.concordium.sdk.responses.Epoch;
import com.concordium.sdk.responses.Round;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * A timeout certificate is the certificate that the finalization committee issues when a round times out,
 * thus making it possible for the protocol to proceed to the next round.
 */
@Builder
@Getter
@EqualsAndHashCode
@ToString
public class TimeoutCertificate {
    /**
     * The round that timed out.
     */
    private final Round round;
    /**
     * The minimum epoch of which signatures are included in the {@link TimeoutCertificate#aggregateSignature}.
     */
    private final Epoch minEpoch;
    /**
     * The rounds of which finalizers have their best QCs in the {@link TimeoutCertificate#minEpoch}.
     */
    private final List<FinalizerRound> qcRoundsFirstEpoch;
    /**
     * The rounds of which finalizers have their best QCs in the epoch {@link TimeoutCertificate#minEpoch} + 1.
     */
    private final List<FinalizerRound> qcRoundsSecondEpoch;
    /**
     * The aggregated signature by the finalization committee that witnessed the {@link TimeoutCertificate#round} timed out.
     */
    private final TimeoutSignature aggregateSignature;

    /**
     * Parses {@link com.concordium.grpc.v2.TimeoutCertificate} to {@link TimeoutCertificate}.
     * @param certificate {@link com.concordium.grpc.v2.TimeoutCertificate} returned by the GRPC v2 API.
     * @return parsed {@link TimeoutCertificate}.
     */
    public static TimeoutCertificate from(com.concordium.grpc.v2.TimeoutCertificate certificate) {
        val firstEpochList = new ArrayList<FinalizerRound>();
        val secondEpochList = new ArrayList<FinalizerRound>();
        certificate.getQcRoundsFirstEpochList().forEach(
                f -> firstEpochList.add(FinalizerRound.from(f))
        );
        certificate.getQcRoundsSecondEpochList().forEach(
                f -> secondEpochList.add(FinalizerRound.from(f))
        );
        return TimeoutCertificate.builder()
                .round(Round.from(certificate.getRound()))
                .minEpoch(Epoch.from(certificate.getMinEpoch()))
                .qcRoundsFirstEpoch(firstEpochList)
                .qcRoundsSecondEpoch(secondEpochList)
                .aggregateSignature(TimeoutSignature.from(certificate.getAggregateSignature()))
                .build();
    }
}
