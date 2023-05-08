package com.concordium.sdk.responses.transactionevent.updatepayloads;

import com.concordium.grpc.v2.CooldownParametersCpv1;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Duration;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class CooldownParametersCPV1UpdatePayload implements UpdatePayload {

    /**
     * Number of seconds that pool owners must cooldown when reducing their equity capital or closing the pool.
     */
    private Duration poolOwnerCooldown;

    /**
     * Number of seconds that a delegator must cooldown when reducing their delegated stake.
     */
    private UInt64 delegatorCooldown;

    public static CooldownParametersCPV1UpdatePayload parse(CooldownParametersCpv1 cooldownParametersCpv1) {
        return CooldownParametersCPV1UpdatePayload.builder()
                .poolOwnerCooldown(Duration.ofSeconds(cooldownParametersCpv1.getPoolOwnerCooldown().getValue()))
                .delegatorCooldown(UInt64.from(cooldownParametersCpv1.getDelegatorCooldown().getValue()))
                .build();
    }

    @Override
    public UpdateType getType() {
        return UpdateType.COOLDOWN_PARAMETERS_CPV_1_UPDATE;
    }
}
