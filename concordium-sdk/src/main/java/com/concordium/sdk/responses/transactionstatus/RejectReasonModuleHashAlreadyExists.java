package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.ModuleRef;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.codec.binary.Hex;

/**
 * The module already exists.
 */
@Getter
@ToString
public class RejectReasonModuleHashAlreadyExists extends RejectReason {
    private final String moduleRef;

    @JsonCreator
    RejectReasonModuleHashAlreadyExists(@JsonProperty("contents") String moduleRef) {
        this.moduleRef = moduleRef;
    }

    /**
     * Parses {@link ModuleRef} to {@link RejectReasonModuleHashAlreadyExists}.
     * @param moduleHashAlreadyExists {@link ModuleRef} returned by the GRPC V2 API.
     * @return parsed {@link RejectReasonModuleHashAlreadyExists}.
     */
    public static RejectReasonModuleHashAlreadyExists parse(ModuleRef moduleHashAlreadyExists) {
        return new RejectReasonModuleHashAlreadyExists(Hex.encodeHexString(moduleHashAlreadyExists.getValue().toByteArray()));
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.MODULE_HASH_ALREADY_EXISTS;
    }
}
