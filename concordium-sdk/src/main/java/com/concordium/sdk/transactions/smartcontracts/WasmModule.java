package com.concordium.sdk.transactions.smartcontracts;

import com.concordium.sdk.crypto.SHA256;
import com.concordium.sdk.transactions.TransactionType;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.Getter;
import lombok.val;
import org.apache.commons.codec.binary.Hex;

import java.nio.ByteBuffer;
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

    /**
     * Get the identifier of the WasmModule.
     * The identifier is a SHA256 hash of the raw module bytes.
     * @return the identifier of the Module.
     */
    public String getIdentifier() {
        return Hex.encodeHexString(SHA256.hash(this.moduleSource.getBytes()));
    }

    /**
     * Get the cost of deploying the module.
     *
     * @return the Energy cost.
     */
    public UInt64 getCost() {
        return UInt64.from(moduleSource.getSize() / 10);
    }

    /**
     * Get the raw serialized bytes of the concrete {@link WasmModule}.
     * This can currently either be {@link WasmModuleSourceV0} or {@link WasmModuleSourceV1}
     *
     * @return bytes
     */
    public byte[] getBytes() {
        val buffer = ByteBuffer.allocate(TransactionType.BYTES  + moduleSource.getSize());
        buffer.put(TransactionType.DEPLOY_MODULE.getValue());
        buffer.put(moduleSource.getBytes());
        return buffer.array();
    }
}
