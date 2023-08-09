package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.CryptoJniNative;
import com.concordium.sdk.crypto.NativeResolver;
import com.concordium.sdk.exceptions.CryptoJniException;
import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.codec.binary.Hex;

@Getter
public abstract class SchemaInitParameter {

    //static block to load native library
    static {
        NativeResolver.loadLib();
    }

    /**
     * Schema of the module. TODO better comment
     */
    @JsonIgnore
    private final Schema schema;
    @JsonIgnore
    private final InitName initName;
    @JsonIgnore
    private boolean initialized; // @JsonIgnore does not work for name: 'isInitialized'
    @JsonIgnore
    @Getter(AccessLevel.NONE)   // Does this need to be available? getBytes() should always be used for this as it checks for initialization?
                                // Should this be stored as a String or just converted to a byte[] at once?
    private String serializedParameter;

    protected SchemaInitParameter(Schema schema, InitName initName) {
        this.schema = schema;
        this.initName = initName;
        this.initialized = false;
        this.serializedParameter = "";
    }

    /**
     * TODO comment
     * @throws IllegalArgumentException
     * @throws
     *
     *
     * //
     */
    public void initialize() {
        initialize(false);
    }

    /**
     * TODO comment
     * @param verboseErrors
     */

    public void initialize(boolean verboseErrors) {
        String contractName = initName.getName();
        byte[] schemaBytes = schema.getSchemaBytes();
        SchemaVersion schemaVersion = schema.getVersion();
        SerializeParameterResult result = null; // The other classes using the jni initializes the result as null. Why ?
        try {
            String parameterJson = JsonMapper.INSTANCE.writeValueAsString(this);
            val resultJson = CryptoJniNative.serializeInitParameter(parameterJson, contractName, schemaBytes, schemaVersion.getVersion(), verboseErrors);
            result = JsonMapper.INSTANCE.readValue(resultJson, SerializeParameterResult.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if (!result.isSuccess()) {
            throw CryptoJniException.from(result.getErr());
        }
        serializedParameter = result.getSerializedParameter();
        this.initialized = true;
    }

    /**
     * TODO comment
     * @return
     */
    @SneakyThrows // Is this okay? The hex should always be valid? maybe?
    public byte[] toBytes() {
        if (!initialized) {throw new IllegalStateException("Must initialize parameter before passing to method");}
        return Hex.decodeHex(serializedParameter);
    }
}
