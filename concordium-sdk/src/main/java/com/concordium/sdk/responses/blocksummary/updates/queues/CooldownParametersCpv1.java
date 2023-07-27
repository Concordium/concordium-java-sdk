package com.concordium.sdk.responses.blocksummary.updates.queues;

import com.concordium.sdk.responses.chainparameters.CooldownParameter;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Cooldown parameters
 */
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@Builder
public final class CooldownParametersCpv1 extends CooldownParameter {
    /**
     * Pool owner cooldown in seconds
     */
    private final long poolOwnerCooldown;

    /**
     * Delegator cooldown in seconds.
     */
    private final long delegatorCooldown;

    public static CooldownParameter from(com.concordium.grpc.v2.CooldownParametersCpv1 cooldownParametersCpv1Update) {
        return CooldownParametersCpv1
                .builder()
                .poolOwnerCooldown(cooldownParametersCpv1Update.getPoolOwnerCooldown().getValue())
                .delegatorCooldown(cooldownParametersCpv1Update.getDelegatorCooldown().getValue())
                .build();
    }

    @Override
    public CooldownParameterVersion getVersion() {
        return CooldownParameterVersion.V2;
    }
}
