package com.concordium.sdk.responses.accountinfo;

import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.Timestamp;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

/**
 * Records the amount, (expected) release time and whether it's a regular cooldown,
 * pre-cooldown or pre-pre-cooldown.
 */
@Data
@Jacksonized
@Builder
public final class Cooldown {
    /**
     * The time in milliseconds since the Unix epoch when the cooldown period ends.
     */
    private final Timestamp endTime;
    /**
     * The amount that is in cooldown and set to be released at the end of the
     * cooldown period.
     */
    private final CCDAmount amount;
    /**
     * The status of the cooldown.
     */
    private final CooldownStatus status;

    public enum CooldownStatus {
        COOLDOWN,
        PRE_COOLDOWN,
        PRE_PRE_COOLDOWN,
        ;

        public static CooldownStatus from(com.concordium.grpc.v2.Cooldown.CooldownStatus cooldownStatus) {
            switch (cooldownStatus) {
                case COOLDOWN:
                    return COOLDOWN;
                case PRE_COOLDOWN:
                    return PRE_COOLDOWN;
                case PRE_PRE_COOLDOWN:
                    return PRE_PRE_COOLDOWN;
                default:
                    throw new IllegalArgumentException("Unrecognized cooldown status.");
            }
        }
    }
}
