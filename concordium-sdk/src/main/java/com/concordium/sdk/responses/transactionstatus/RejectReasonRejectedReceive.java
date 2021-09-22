package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class RejectReasonRejectedReceive extends RejectReasonContent {
    private final int rejectReason;
    private final ContractAddress contractAddress;
    private final String receiveName;
    private final String parameter;

    @JsonCreator
    RejectReasonRejectedReceive(@JsonProperty("rejectReason") int rejectReason,
                                @JsonProperty("contractAddress") ContractAddress contractAddress,
                                @JsonProperty("receiveName") String receiveName,
                                @JsonProperty("parameter") String parameter) {
        this.rejectReason = rejectReason;
        this.contractAddress = contractAddress;
        this.receiveName = receiveName;
        this.parameter = parameter;
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.REJECTED_RECEIVE;
    }
}
