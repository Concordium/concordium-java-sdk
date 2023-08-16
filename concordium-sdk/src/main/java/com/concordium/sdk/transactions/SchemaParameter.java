package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.CryptoJniNative;
import com.concordium.sdk.crypto.NativeResolver;
import com.concordium.sdk.exceptions.CryptoJniException;
import com.concordium.sdk.exceptions.JNIError;
import com.concordium.sdk.requests.smartcontracts.InvokeInstanceRequest;
import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Hex;

/**
 * Wrapper for parameters serializable via a {@link Schema} to be used with smart contracts.
 * Must be initialized by calling {@link SchemaParameter#initialize()} before usage.
 * Implementing subclasses must be JSON serializable in accordance with the provided smart contract schema.
 * If default serialization does not work, a custom serializer should be implemented and used with annotation @JsonSerialize (using = <MyCustomSerializer>.class).
 * See TODO link example. for example.
 */
@Getter
public abstract class SchemaParameter {

    //static block to load native library
    static {
        NativeResolver.loadLib();
    }

    /**
     * {@link Schema} of the module.
     */
    @JsonIgnore
    private final Schema schema;
    /**
     * {@link InitName} containing the name of the contract to be initialized with the parameter.
     * Populated iff type is {@link ParameterType#INIT}
     */
    @JsonIgnore
    private final InitName initName;
    /**
     * {@link ReceiveName} containing the name of the contract and method to be updated/invoked with the parameter.
     * Populated iff type is {@link ParameterType#RECEIVE}.
     */
    @JsonIgnore
    private final ReceiveName receiveName;
    /**
     * Whether the parameter has been initialized. Parameter should always be initialized with {@link SchemaParameter#initialize()} before being used to create initialize/update/invoke requests.
     */
    @JsonIgnore
    private boolean initialized;
    /**
     * Type of the parameter.
     * {@link ParameterType#INIT} to be used with {@link InitContractPayload}.
     * {@link ParameterType#RECEIVE} to be used with {@link UpdateContractPayload} and {@link InvokeInstanceRequest}.
     */
    @JsonIgnore
    private final ParameterType type;
    /**
     * The serialized parameter, is a hex encoded byte array. Only populated after {@link SchemaParameter#initialize()} has been called.
     */
    @JsonIgnore
    @Getter(AccessLevel.NONE)  // Does this need to be available? getBytes() should always be used for this as it checks for initialization?
                               // Should this be stored as a String or just converted to a byte[] at once?
    private String serializedParameter;

    /**
     * Creates a {@link SchemaParameter} of type {@link ParameterType#INIT} for use with {@link InitContractPayload}.
     * Parameter must be initialized with {@link SchemaParameter#initialize()} before use.
     * @param schema {@link Schema} of the module.
     * @param initName The name of the contract to be initialized with the parameter.
     */
    protected SchemaParameter(Schema schema, InitName initName) {
        this.schema = schema;
        this.receiveName = null;
        this.initName = initName;
        this.type = ParameterType.INIT;
        this.initialized = false;
        this.serializedParameter = "";
    }

    /**
     * Creates a {@link SchemaParameter} of type {@link ParameterType#RECEIVE} for use with {@link UpdateContractPayload} and {@link InvokeInstanceRequest}.
     * Parameter must be initialized with {@link SchemaParameter#initialize()} before use.
     * @param schema {@link Schema} of the module.
     * @param receiveName {@link ReceiveName} containing the name of the contract and method to be updated/invoked with the parameter.
     */
    protected SchemaParameter(Schema schema, ReceiveName receiveName) {
        this.schema = schema;
        this.receiveName = receiveName;
        this.initName = null;
        this.type = ParameterType.RECEIVE;
        this.initialized = false;
        this.serializedParameter = "";
    }


    /**
     * Initializes the parameter by ensuring serialization is performed correctly. This is required before parameter is used.
     * Use {@link SchemaParameter#initialize(boolean)} with parameter 'true' for more descriptive errors.
     * @throws CryptoJniException if the serialization could not be performed.
     */
    public void initialize() {
        initialize(false);
    }

    /**
     * Initializes the parameter by ensuring serialization is performed correctly. This is required before parameter is used.
     * @param verboseErrors whether to return errors in a verbose format or not. Defaults to false if omitted.
     * @throws CryptoJniException if the serialization could not be performed.
     */
    public void initialize(boolean verboseErrors) {
        byte[] schemaBytes = schema.getSchemaBytes();
        SchemaVersion schemaVersion = schema.getVersion();
        SerializeParameterResult result = getSerializeParameterResult(verboseErrors, schemaBytes, schemaVersion);
        if (!result.isSuccess()) {
            throw CryptoJniException.from(result.getErr());
        }
        serializedParameter = result.getSerializedParameter();
        this.initialized = true;
    }

    /**
     * Helper method for {@link SchemaParameter#initialize()}. Performs the JNI call and parses result to {@link SerializeParameterResult}.
     * @param verboseErrors whether to return verbose errors.
     * @param schemaBytes byte[] containing the schema.
     * @param schemaVersion version of the schema.
     * @return {@link SerializeParameterResult} containing the serialized parameter if successful or a {@link JNIError} if not.
     */
    private SerializeParameterResult getSerializeParameterResult(boolean verboseErrors, byte[] schemaBytes, SchemaVersion schemaVersion) {
        SerializeParameterResult result;
        try {
            String parameterJson = JsonMapper.INSTANCE.writeValueAsString(this);
            String resultJson;
            if (this.type == ParameterType.INIT) {
                String contractName = initName.getName();
                resultJson = CryptoJniNative.serializeInitParameter(parameterJson, contractName, schemaBytes, schemaVersion.getVersion(), verboseErrors);
            } else {
                String contractName = receiveName.getContractName();
                String methodName = receiveName.getMethod();
                resultJson = CryptoJniNative.serializeReceiveParameter(parameterJson, contractName, methodName, schemaBytes, schemaVersion.getVersion(), verboseErrors);
            }
            result = JsonMapper.INSTANCE.readValue(resultJson, SerializeParameterResult.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * Converts the serialized parameter to a byte[]. Should only be called after parameter is initialized with {@link SchemaParameter#initialize()}.
     * @throws IllegalStateException if parameter is not initialized.
     * @return byte[] containing the serialized parameter.
     */
    @SneakyThrows // Is this okay? The hex should always be valid? maybe?
    public byte[] toBytes() {
        if (!initialized) {throw new IllegalStateException("Must initialize parameter before use");}
        return Hex.decodeHex(serializedParameter);
    }
}
