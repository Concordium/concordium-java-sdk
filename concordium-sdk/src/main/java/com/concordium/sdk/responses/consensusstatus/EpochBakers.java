package com.concordium.sdk.responses.consensusstatus;

import com.concordium.sdk.responses.Epoch;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Optional;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class EpochBakers {
    /**
     * The bakers and finalizers for the previous epoch.
     * If the current epoch is 0, then this is the same as the bakers for the current epoch.
     */
    private final BakersAndFinalizers previousEpochBakers;

    public Optional<BakersAndFinalizers> getPreviousEpochBakers() {
        return Optional.ofNullable(previousEpochBakers);
    }

    /**
     * The bakers and finalizers for the current epoch.
     * If this is absent, it should be treated as the same as the bakers for the previous epoch.
     */
    private final BakersAndFinalizers currentEpochBakers;

    public Optional<BakersAndFinalizers> getCurrentEpochBakers() {
        return Optional.ofNullable(currentEpochBakers);
    }

    /**
     * The bakers and finalizers for the next epoch.
     * If this is absent, it should be treated as the same as the bakers for the current epoch.
     */
    private final BakersAndFinalizers nextEpochBakers;

    public Optional<BakersAndFinalizers> getNextEpochBakers() {
        return Optional.ofNullable(nextEpochBakers);
    }

    /**
     * The first epoch of the next payday.
     */
    private final Epoch nextPayday;
}
