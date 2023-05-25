package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.codec.binary.Hex;

import java.util.List;

/**
 * Reference to a non-existing contract receive method.
 */
@Getter
@ToString
@AllArgsConstructor
@Builder
class RejectReasonInvalidReceiveMethod extends RejectReason {
    private final String moduleRef;
    private final String receiveName;

    @JsonCreator
    public RejectReasonInvalidReceiveMethod(@JsonProperty("contents") List<String> contents) {
        if (contents.size() != 2) {
            throw new IllegalArgumentException("Unable to parse RejectReasonInvalidReceiveMethod. Unexpected contents size.");
        }
        this.receiveName = contents.get(0);
        this.moduleRef = contents.get(1);
    }

    /**
     * Parses {@link com.concordium.grpc.v2.RejectReason.InvalidReceiveMethod} to {@link RejectReasonInvalidReceiveMethod}.
     * @param invalidReceiveMethod {@link com.concordium.grpc.v2.RejectReason.InvalidReceiveMethod} returned by the GRPC V2 API.
     * @return parsed {@link RejectReasonInvalidReceiveMethod}.
     */
    public static RejectReasonInvalidReceiveMethod parse(com.concordium.grpc.v2.RejectReason.InvalidReceiveMethod invalidReceiveMethod) {
        return RejectReasonInvalidReceiveMethod.builder()
                .moduleRef(Hex.encodeHexString(invalidReceiveMethod.getModuleRef().getValue().toByteArray()))
                .receiveName(invalidReceiveMethod.getReceiveName().getValue())
                .build();
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_RECEIVE_METHOD;
    }
}
