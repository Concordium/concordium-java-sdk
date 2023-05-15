package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.codec.binary.Hex;

import java.util.List;

/**
 * Reference to a non-existing contract init method.
 */
@Getter
@ToString
@Builder
public class RejectReasonInvalidInitMethod extends RejectReason {
    private final String moduleRef;
    private final String initName;

    @JsonCreator
    RejectReasonInvalidInitMethod(@JsonProperty("contents") List<String> contents) {
        if (contents.size() != 2) {
            throw new IllegalArgumentException("Unable to parse RejectReasonInvalidInitMethod. Unexpected contents size.");
        }
        this.initName = contents.get(0);
        this.moduleRef = contents.get(1);

    }

    /**
     * Parses {@link com.concordium.grpc.v2.RejectReason.InvalidInitMethod} to {@link RejectReasonInvalidInitMethod}.
     * @param invalidInitMethod {@link com.concordium.grpc.v2.RejectReason.InvalidInitMethod} returned by the GRPC V2 API.
     * @return parsed {@link RejectReasonInvalidInitMethod}.
     */
    public static RejectReasonInvalidInitMethod parse(com.concordium.grpc.v2.RejectReason.InvalidInitMethod invalidInitMethod) {
        return RejectReasonInvalidInitMethod.builder()
                .moduleRef(Hex.encodeHexString(invalidInitMethod.getModuleRef().getValue().toByteArray()))
                .initName(invalidInitMethod.getInitName().getValue())
                .build();
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_INIT_METHOD;
    }
}
