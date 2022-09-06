package com.concordium.sdk.transactions.smartcontracts;

import com.concordium.sdk.crypto.SHA256;
import com.concordium.sdk.transactions.TransactionType;
import lombok.Builder;
import lombok.Getter;
import lombok.val;
import org.apache.commons.codec.binary.Hex;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;

@Getter
public class WasmModule {

    private final WasmModuleVersion version;
    private final WasmModuleSource moduleSource;

    @Builder
    public WasmModule(WasmModuleVersion version, WasmModuleSource moduleSource) {
        if (Objects.isNull(moduleSource)) {
            throw new IllegalArgumentException("WasmModuleSource cannot be null");
        }
        this.version = version;
        this.moduleSource = moduleSource;
    }

    public static WasmModule from(byte[] fileBuffer, int version) {
        WasmModuleSource moduleSource;
        WasmModuleVersion moduleVersion;
        fileBuffer = Arrays.copyOfRange(fileBuffer, 8, fileBuffer.length);

        if (version == 1) {
            moduleSource = WasmModuleSourceV1.from(fileBuffer);
            moduleVersion = WasmModuleVersion.V1;
        }
        else {
            moduleSource = WasmModuleSourceV0.from(fileBuffer);
            moduleVersion = WasmModuleVersion.V0;
        }
        return new WasmModule(moduleVersion, moduleSource);
    }

    /**
     * Get the identifier of the WasmModule.
     * The identifier is a SHA256 hash of the raw module bytes.
     * @return the identifier of the Module.
     */
    public String getIdentifier() {
        return Hex.encodeHexString(SHA256.hash(this.moduleSource.getBytes()));
    }

    /**
     * Get the raw serialized bytes of the concrete {@link WasmModule}.
     * This can currently either be {@link WasmModuleSourceV0} or {@link WasmModuleSourceV1}
     *
     * @return bytes
     */

    public byte[] getBytes() {
        val moduleSourceBytes = moduleSource.getBytes();
        val versionBytes = version.getBytes();
        val buffer = ByteBuffer.allocate(TransactionType.BYTES + moduleSourceBytes.length + versionBytes.length);
        buffer.put(TransactionType.DEPLOY_MODULE.getValue());
        buffer.put(versionBytes);
        buffer.put(moduleSourceBytes);
        return buffer.array();
    }
}
