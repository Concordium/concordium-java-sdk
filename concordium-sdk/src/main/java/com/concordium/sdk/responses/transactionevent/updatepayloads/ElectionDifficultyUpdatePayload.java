package com.concordium.sdk.responses.transactionevent.updatepayloads;

import com.concordium.grpc.v2.ElectionDifficulty;
import com.concordium.sdk.responses.blocksummary.updates.Fraction;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class ElectionDifficultyUpdatePayload implements UpdatePayload {

    /**
     * TODO
     *
     * Parts per hundred thousand.
     */
    private Fraction value;

    /**
     * Parses {@link ElectionDifficulty} to {@link ElectionDifficultyUpdatePayload}.
     * @param electionDifficulty {@link ElectionDifficulty} returned by the GRPC V2 API.
     * @return parsed {@link ElectionDifficultyUpdatePayload}.
     */
    public static ElectionDifficultyUpdatePayload parse(ElectionDifficulty electionDifficulty) {
        return ElectionDifficultyUpdatePayload.builder()
                .value(Fraction.from(electionDifficulty.getValue()))
                .build();
    }
    @Override
    public UpdateType getType() {
        return UpdateType.ELECTION_DIFFICULTY_UPDATE;
    }
}
