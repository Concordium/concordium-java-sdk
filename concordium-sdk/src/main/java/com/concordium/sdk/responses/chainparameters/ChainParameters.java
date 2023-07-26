package com.concordium.sdk.responses.chainparameters;

/**
 * Base class for all chain parameters.
 * All chain parameters must implement 'getVersion'.
 */
public abstract class ChainParameters {

    public abstract Version getVersion();

    /**
     * Chain parameters versions
     */
    public enum Version {
        // Chain parameters effective in protocol 1 to 3.
        CPV0,
        // Chain parameters effective in protocol versions 4 to 5.
        CPV1,
        // Chain parameters effective in protocol version 6.
        CPV2
    }
}
