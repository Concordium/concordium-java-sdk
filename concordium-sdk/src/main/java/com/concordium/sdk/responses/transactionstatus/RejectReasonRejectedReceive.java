package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.types.ContractAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.codec.binary.Hex;

/**
 * Rejected due to contract logic in its receive method.
 */
@Getter
@ToString
@Builder
@EqualsAndHashCode(callSuper = true)
public class RejectReasonRejectedReceive extends RejectReason {
    /**
     * The reject reason
     */
    private final int rejectReason;
    /**
     * The address of the contract which rejected.
     */
    private final ContractAddress contractAddress;
    /**
     * The name of the receive method.
     */
    private final String receiveName;
    /**
     * The parameter the receive method was called with.
     */
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

    /**
     * Parses {@link com.concordium.grpc.v2.RejectReason.RejectedReceive} to {@link RejectReasonRejectedReceive}.
     * @param rejectedReceive {@link com.concordium.grpc.v2.RejectReason.RejectedReceive} returned by the GRPC V2 API.
     * @return parsed {@link RejectReasonRejectedReceive}.
     */
    public static RejectReasonRejectedReceive parse(com.concordium.grpc.v2.RejectReason.RejectedReceive rejectedReceive) {
        return RejectReasonRejectedReceive.builder()
                .rejectReason(rejectedReceive.getRejectReason())
                .contractAddress(ContractAddress.parse(rejectedReceive.getContractAddress()))
                .receiveName(rejectedReceive.getReceiveName().getValue())
                .parameter(Hex.encodeHexString(rejectedReceive.getParameter().getValue().toByteArray()))
                .build();
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.REJECTED_RECEIVE;
    }
}
