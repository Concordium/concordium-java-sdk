package com.concordium.sdk.responses.accountinfo;

import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

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
        this.schedule = ImmutableList.copyOf(newSchedule);
    }
}
