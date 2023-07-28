package com.concordium.sdk.responses.chainparameters;

import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode(callSuper = false)
public class CooldownInEpochs extends CooldownParameter {
    private final UInt64 numberOfEpochs;

    public static CooldownInEpochs from(long value) {
        return new CooldownInEpochs(UInt64.from(value));
    }

    @Override
    public CooldownParameterVersion getVersion() {
        return CooldownParameterVersion.V1;
    }
}
