package com.concordium.sdk.responses.accountinfo;

import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
@ToString
@EqualsAndHashCode
@Jacksonized
@Builder
public final class ReleaseSchedule {
    private final CCDAmount total;
    private final ImmutableList<ScheduledRelease> schedule;

    @Builder
    ReleaseSchedule(@JsonProperty("total") CCDAmount total,
                    @Singular(value = "scheduleRelease") @JsonProperty("schedule") List<ScheduledRelease> newSchedule) {
        this.total = total;
        if(Objects.isNull(newSchedule)) {
            this.schedule = ImmutableList.of();
        } else {
            this.schedule = ImmutableList.copyOf(newSchedule);

        }
    }
}
