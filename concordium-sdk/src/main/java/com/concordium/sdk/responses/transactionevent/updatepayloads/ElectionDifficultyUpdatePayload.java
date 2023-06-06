package com.concordium.sdk.responses.transactionevent.updatepayloads;

import com.concordium.grpc.v2.ElectionDifficulty;
import com.concordium.sdk.responses.transactionstatus.PartsPerHundredThousand;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * The election difficulty was updated.
 */
@Builder
@Getter
@EqualsAndHashCode
@ToString
public class ElectionDifficultyUpdatePayload implements UpdatePayload {

    /**
     * Election difficulty parameter.
     */
    private PartsPerHundredThousand value;

    /**
     * Parses {@link ElectionDifficulty} to {@link ElectionDifficultyUpdatePayload}.
     * @param electionDifficulty {@link ElectionDifficulty} returned by the GRPC V2 API.
     * @return parsed {@link ElectionDifficultyUpdatePayload}.
     */
    public static ElectionDifficultyUpdatePayload parse(ElectionDifficulty electionDifficulty) {
        return ElectionDifficultyUpdatePayload.builder()
                .value(PartsPerHundredThousand.parse(electionDifficulty.getValue()))
                .build();
    }
    @Override
    public UpdateType getType() {
        return UpdateType.ELECTION_DIFFICULTY_UPDATE;
    }
}
