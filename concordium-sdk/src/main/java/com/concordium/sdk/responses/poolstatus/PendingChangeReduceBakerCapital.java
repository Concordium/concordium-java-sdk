package com.concordium.sdk.responses.poolstatus;

import com.concordium.sdk.transactions.CCDAmount;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Getter
@ToString
@Data
public class PendingChangeReduceBakerCapital extends PendingChange {

    /**
     * New baker equity capital.
     */
    private CCDAmount bakerEquityCapital;

    /**
     * Effective time of the change.
     */
    private Date effectiveTime;
}
