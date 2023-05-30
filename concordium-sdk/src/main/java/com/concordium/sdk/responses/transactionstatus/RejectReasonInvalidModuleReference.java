package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.ModuleRef;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.codec.binary.Hex;

/**
 * Reference to a non-existing module.
 */
@ToString
@EqualsAndHashCode
public class RejectReasonInvalidModuleReference extends RejectReason {
    @Getter
    private final String moduleRef;

    @JsonCreator
    RejectReasonInvalidModuleReference(@JsonProperty("contents") String moduleRef) {
        this.moduleRef = moduleRef;
    }

    /**
     * Parses {@link ModuleRef} to {@link RejectReasonInvalidModuleReference}.
     * @param invalidModuleReference {@link ModuleRef} returned by the GRPC V2 API.
     * @return parsed {@link RejectReasonInvalidModuleReference}.
     */
    public static RejectReasonInvalidModuleReference parse(ModuleRef invalidModuleReference) {
        return new RejectReasonInvalidModuleReference(Hex.encodeHexString(invalidModuleReference.getValue().toByteArray()));
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_MODULE_REFERENCE;
    }
}
