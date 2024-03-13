package com.concordium.sdk.transactions.smartcontracts;

import com.concordium.sdk.crypto.SHA256;
import com.concordium.sdk.responses.modulelist.ModuleRef;
import com.concordium.sdk.types.LEB128U;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.val;
import org.bouncycastle.util.Strings;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

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
     * Create {@link WasmModule} from compiled module WASM file. Passed module should have version prefixed.
     * @param path path to the compiled WASM module.
     * @return parsed {@link WasmModule}.
     * @throws IOException if an I/O exception occurs reading from the provided path.
     */
    public static WasmModule from(String path) throws IOException {
        val moduleBytes = Files.readAllBytes(Paths.get(path));
        return from(moduleBytes);
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

    /**
     * Retrieve the {@link Schema} corresponding to the contract, if embedded.
     * Behaviour is not specified if the bytes of {@link WasmModule} do not represent a valid concordium wasm module.
     * @return {@link Optional} containing the {@link Schema} if found, empty otherwise.
     */
    public Optional<Schema> getSchema() {
        val moduleSourceBytes = source.getBytes();
        val buffer = ByteBuffer.wrap(moduleSourceBytes.clone());

        // Skip 4 byte length of WasmModuleSource (UInt32.BYTES) + 4 byte magic number + 4 byte WASM version
        buffer.position(buffer.position() + 12);

        while (buffer.hasRemaining()) {
            // A section is a 1 byte id followed by the length of the section as a LEB128U encoded u32.
            byte id = buffer.get();
            int remainingSectionLength = LEB128U.decode(buffer, LEB128U.U32_BYTES).intValue();

            // Custom sections have id 0 så all other ids are skipped.
            if (id != 0) {
                buffer.position(buffer.position() + remainingSectionLength);
                continue;
            }

            // Custom sections have a name encoded as a vector i.e. a length followed by the actual bytes
            int beforeName = buffer.position();
            int nameLength = LEB128U.decode(buffer, LEB128U.U32_BYTES).intValue();
            int nameLengthBytes = buffer.position() - beforeName;

            byte[] nameBytes = new byte[nameLength];
            buffer.get(nameBytes);

            String name = Strings.fromByteArray(nameBytes);

            // We've incremented the buffer by reading the length of the name and the name.
            remainingSectionLength = remainingSectionLength - nameLengthBytes - nameLength;

            if (name.equals("concordium-schema")) {
                // After reading the name, the remaining contents of the custom section is the schema itself.
                byte[] schemaBytes = new byte[remainingSectionLength];
                buffer.get(schemaBytes);
                Schema schema = Schema.from(schemaBytes);
                return Optional.of(schema);
            }

            // Go to the next section if the name didn't match.
            buffer.position(buffer.position() + remainingSectionLength);
        }

        return Optional.empty();
    }

}
