package com.concordium.sdk.responses.poolstatus;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Getter
@ToString
@Data
public class PendingChangeRemovePool extends PendingChange {

    /**
     * Effective time of the change.
     */
    private Date effectiveTime;
}
