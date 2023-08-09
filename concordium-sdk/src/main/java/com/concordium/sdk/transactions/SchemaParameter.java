package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.CryptoJniNative;
import com.concordium.sdk.crypto.NativeResolver;
import com.concordium.sdk.exceptions.CryptoJniException;
import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.codec.DecoderException;

/**
 * TODO comment.
 */
@Getter
public abstract class SchemaParameter {

    static {
        NativeResolver.loadLib();
    }

    /**
     * Schema of the module. TODO better comment
     */
    @JsonIgnore
    private final Schema schema;
    @JsonIgnore
    private final ReceiveName receiveName;
    @JsonIgnore
    private boolean initialized; // @JsonIgnore does not work for name: 'isInitialized'
    @JsonIgnore
    private String serializedParameter;

    protected SchemaParameter(Schema schema, ReceiveName receiveName) {
        this.schema = schema;
        this.receiveName = receiveName;
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
    @SneakyThrows
    public void initialize(boolean verboseErrors) {
        String json = JsonMapper.INSTANCE.writeValueAsString(this);
        System.out.println(json);
        String method = receiveName.getMethod();
        String contractName = receiveName.getContractName();
        byte[] schemaBytes = schema.getSchemaBytes();
        SchemaVersion version = schema.getVersion();
        SerializeParameterResult result = null;
        try {
            val jsonStr = CryptoJniNative.serializeParameter(json, contractName, method, schemaBytes, version.getVersion(), verboseErrors);
            result = JsonMapper.INSTANCE.readValue(jsonStr, SerializeParameterResult.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(result);
        if (!result.isSuccess()) {
            throw CryptoJniException.from(
                    result.getErr()
            );
        }
        this.initialized = true;
        serializedParameter = result.getSerializedParameter();
    }

    /**
     * TODO comment
     * @return
     */
    public String toBytes() throws JsonProcessingException, DecoderException {
        if (!initialized) {throw new IllegalStateException("Must initialize parameter before passing to method");}
        return serializedParameter;
    }
}
