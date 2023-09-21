package com.concordium.sdk.transactions.smartcontracts;

import com.concordium.sdk.crypto.SHA256;
import com.concordium.sdk.responses.modulelist.ModuleRef;
import com.concordium.sdk.transactions.Payload;
import com.concordium.sdk.transactions.TransactionType;
import com.concordium.sdk.types.UInt64;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * A compiled Smart Contract Module in WASM with source and version.
 */
@Getter
@EqualsAndHashCode
@ToString
public class WasmModule {

    private static final long MAX_SIZE_V0 = 65536; // 64 kb
    private static final long MAX_SIZE_V1 = 8 * 65536; // 512 kb

    /**
     * No of bytes reserved to keep Module Version Information.
     */
    static final int VERSION_BYTES = 8;


    /**
     * Module version.
     */
    private final WasmModuleVersion version;

    /**
     * Module Source
     */
    private final WasmModuleSource source;

    WasmModule(final WasmModuleSource source, final WasmModuleVersion version) {
        this.source = source;
        this.version = version;
    }

    /**
     * Create {@link WasmModule} from compiled module WASM file bytes.
     *
     * @param bytes   Complied WASM module file bytes.
     * @param version WASM module version ({@link WasmModuleVersion}). If the version is prefixed use {@link WasmModule#from(byte[])}
     * @return Parsed {@link WasmModule}
     */
    public static WasmModule from(final byte[] bytes, final WasmModuleVersion version) {
        switch (version) {
            case V0:
                if (bytes.length > MAX_SIZE_V0) {
                    throw new IllegalArgumentException("Wasm V0 modules may not exceed " + MAX_SIZE_V0 + " bytes.");
                }
                break;
            case V1:
                if (bytes.length > MAX_SIZE_V1) {
                    throw new IllegalArgumentException("Wasm V1 modules may not exceed " + MAX_SIZE_V1 + " bytes.");
                }
                break;
        }

        return new WasmModule(WasmModuleSource.from(bytes), version);
    }

    /**
     * Create {@link WasmModule} from compiled module WASM file bytes. Passed module bytes should have version prefixed.
     *
     * @param bytes Complied WASM module file bytes.
     * @return Parsed {@link WasmModule}
     */
    public static WasmModule from(final byte[] bytes) {
        val versionBytes = ByteBuffer.wrap(Arrays.copyOfRange(bytes, 0, VERSION_BYTES));
        val version = WasmModuleVersion.from(versionBytes);
        val moduleBytes = Arrays.copyOfRange(bytes, VERSION_BYTES, bytes.length);

        return from(moduleBytes, version);
    }

    /**
     * Get the identifier of the WasmModule.
     * The identifier is a SHA256 hash of the raw module bytes.
     *
     * @return the identifier of the Module.
     */
    public ModuleRef getIdentifier() {
        return ModuleRef.from(SHA256.hash(this.source.getBytes()));
    }

    /**
     * Get the raw serialized bytes of the concrete {@link WasmModule}.
     *
     * @return bytes
     */
    public byte[] getBytes() {
        val moduleSourceBytes = source.getBytes();
        val buffer = ByteBuffer.allocate(moduleSourceBytes.length + WasmModuleVersion.BYTES);
        buffer.put(version.getBytes());
        buffer.put(moduleSourceBytes);

        return buffer.array();
    }

}
