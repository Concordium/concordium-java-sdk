package com.concordium.sdk.responsetypes.transactionstatus.results;

import com.concordium.sdk.responsetypes.transactionstatus.TransactionResultEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class TransferredWithScheduleResult extends TransactionResultEvent {

    private List<List<String>> amount;
    private String to;
    private String from;
}
