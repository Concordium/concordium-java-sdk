package com.concordium.sdk.responses.modulesource;

import lombok.Getter;

/**
 * Compiled Source of the deployed Module in bytes.
 */
@Getter
public class ModuleSource {

    /**
     * Module source bytes.
     */
    private final byte[] bytes;

    private ModuleSource(byte[] bytes) {
        this.bytes = bytes;
    }

    public static ModuleSource from(final byte[] bytes) {
        return new ModuleSource(bytes);
    }
}
