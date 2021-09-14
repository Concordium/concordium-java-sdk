package com.concordium.sdk.responsetypes.accountinfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ReleaseSchedule {
    private String total;
    private List<ScheduledRelease> schedule;

    @Getter
    @Setter
    @ToString
    private class ScheduledRelease {
        private int releaseTimestamp;
        private String releaseAmount;
        private List<String> releaseTransactions;
    }
}
