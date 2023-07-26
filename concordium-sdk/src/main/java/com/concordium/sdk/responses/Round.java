package com.concordium.sdk.responses;

import com.concordium.sdk.types.UInt64;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * A round in the consensus (applicable for protocol version 6 and onwards)
 * A round is a time duration of which a block is potentially being baked and signed off by
 * the finalization committee.
 * If a block is not being produced within this time frame or not enough weighted signatures from the finalization committee
 * are gathered then the round times out and a timeout certificate is being created (by the finalization committee)
 */
@ToString
@EqualsAndHashCode
@Getter
public class Round {

    /**
     * The round expressed as an unsigned integer.
     */
    private final UInt64 value;

    public Round(UInt64 value) {
        this.value = value;
    }

    public static Round from(long round) {
        return new Round(UInt64.from(round));
    }
}
