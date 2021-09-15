package com.concordium.sdk.responses.accountinfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public final class ReleaseSchedule {
    private final String total;
    private final List<ScheduledRelease> schedule;

    ReleaseSchedule(@JsonProperty("total") String total,
                    @JsonProperty("schedule") List<ScheduledRelease> schedule) {
        this.total = total;
        this.schedule = schedule;
    }
}
