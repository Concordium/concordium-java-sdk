package com.concordium.sdk.responses.chainparameters;

/**
 * Versioning for the cooldown parameters that may exist on chain.
 * There are two kinds of parameters where only one is valid at a time.
 * See {@link CooldownParameterVersion} for details.
 */
public abstract class CooldownParameter {
    public abstract CooldownParameterVersion getVersion();

    /**
     * Versions of cooldown parameters that exist on the chain.
     */
    public enum CooldownParameterVersion {
        // Cooldown parameters for protocol versions 1-3
        V1,
        // Cooldown parameters for protocol version 4 and onwards.
        V2
    }
}
