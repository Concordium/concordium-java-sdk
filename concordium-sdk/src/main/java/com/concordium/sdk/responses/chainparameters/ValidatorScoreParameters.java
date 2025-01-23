package com.concordium.sdk.responses.chainparameters;

import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Parameters that govern validator suspension.
 */
@Getter
@EqualsAndHashCode
@ToString
@Builder
public class ValidatorScoreParameters {

    /**
     * The maximal number of missed rounds before a validator gets suspended.
     */
    private final UInt64 maximumMissedRounds;

    public static ValidatorScoreParameters from(com.concordium.grpc.v2.ValidatorScoreParameters input) {
        return ValidatorScoreParameters.builder()
                .maximumMissedRounds(UInt64.from(input.getMaximumMissedRounds()))
                .build();
    }
}
