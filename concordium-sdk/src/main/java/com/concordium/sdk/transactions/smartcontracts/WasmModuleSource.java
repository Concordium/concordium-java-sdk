package com.concordium.sdk.transactions.smartcontracts;

import lombok.val;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.util.Objects;

public abstract class WasmModuleSource {

    /**
     * The raw bytes of the WasmModule.
     */
    private final byte[] rawSource;

    protected WasmModuleSource(byte[] rawModuleSource) {
        this.rawSource = rawModuleSource;
    }

    public static WasmModuleSource createNew(WasmModuleVersion version, String hexRawModuleSource) {
        if (Objects.isNull(hexRawModuleSource)) {
            throw new IllegalArgumentException("Raw WasmModuleSource cannot be null");
        }
        try {
            val rawSource = Hex.decodeHex(hexRawModuleSource);
            switch (version) {
                case V0:
                    return new WasmModuleSourceV0(rawSource);
                case V1:
                    return new WasmModuleSourceV1(rawSource);
                default:
                    throw new IllegalArgumentException("Unknown WasmModuleVersion " + version);
            }
        } catch (DecoderException e) {
            throw new IllegalArgumentException("Invalid HEX encoding of RawModuleSource", e);
        }

    }

    /**
     * Get the length of the module.
     *
     * @return the length of the module.
     */
    public int getSize() {
        return rawSource.length;
    }

    /**
     * Get the raw bytes of the wasm module.
     *
     * @return the module source bytes.
     */
    byte[] getBytes() {
        return rawSource;
    }

    /**
     * Get the {@link WasmModuleVersion}
     *
     * @return the version of the module.
     */
    abstract WasmModuleVersion getVersion();
}
