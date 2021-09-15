package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public final class RejectReason {
    private final RejectReasonType tag;
    private final Object contents;
    private final String rejectReason;
    private final ContractAddress contractAddress;
    private final String receiveName;
    private final String parameter;

    @JsonCreator
    RejectReason(@JsonProperty("tag") RejectReasonType tag,
                 @JsonProperty("contents") Object contents,
                 @JsonProperty("rejectReason") String rejectReason,
                 @JsonProperty("contractAddress") ContractAddress contractAddress,
                 @JsonProperty("receiveName") String receiveName,
                 @JsonProperty("parameter") String parameter) {
        this.tag = tag;
        this.contents = contents;
        this.rejectReason = rejectReason;
        this.contractAddress = contractAddress;
        this.receiveName = receiveName;
        this.parameter = parameter;
    }
}
