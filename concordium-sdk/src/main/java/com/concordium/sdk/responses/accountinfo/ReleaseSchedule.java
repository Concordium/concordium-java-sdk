package com.concordium.sdk.responses.accountinfo;

import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
public final class ReleaseSchedule {
    private final CCDAmount total;
    private final List<ScheduledRelease> schedule;

    ReleaseSchedule(@JsonProperty("total") CCDAmount total,
                    @JsonProperty("schedule") List<ScheduledRelease> schedule) {
        this.total = total;
        this.schedule = schedule;
    }
}
