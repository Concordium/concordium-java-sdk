package com.concordium.sdk.transactions.smartcontracts;

import com.concordium.sdk.types.UInt32;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.val;
import org.apache.commons.codec.binary.Hex;

import java.nio.ByteBuffer;

/**
 * Compiled source in of the WASM Module.
 */
@EqualsAndHashCode
class WasmModuleSource {

    /**
     * The raw bytes of the WasmModule.
     */
    private final byte[] bytes;

    WasmModuleSource(final byte[] bytes) {
        this.bytes = bytes;
    }

    public static WasmModuleSource from(final byte[] bytes) {
        return new WasmModuleSource(bytes);
    }

    /**
     * Get the raw bytes of the wasm module.
     *
     * @return the module source bytes.
     */
    public byte[] getBytes() {
        val buffer = ByteBuffer.allocate(UInt32.BYTES + bytes.length);
        buffer.put(UInt32.from(bytes.length).getBytes());
        buffer.put(bytes);

        return buffer.array();
    }

    @Override
    public String toString() {
        return Hex.encodeHexString(this.bytes);
    }
}
