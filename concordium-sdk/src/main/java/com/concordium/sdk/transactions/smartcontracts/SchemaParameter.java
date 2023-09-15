package com.concordium.sdk.transactions.smartcontracts;

import com.concordium.sdk.crypto.CryptoJniNative;
import com.concordium.sdk.crypto.NativeResolver;
import com.concordium.sdk.exceptions.CryptoJniException;
import com.concordium.sdk.exceptions.JNIError;
import com.concordium.sdk.requests.smartcontracts.InvokeInstanceRequest;
import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.*;
import com.concordium.sdk.types.AbstractAddress;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Hex;

/**
 * Represents smart contract parameters serializable via a {@link Schema}.
 * <p>
 * Classes representing smart contract parameters should extend this, and ensure that they are JSON serializable in accordance with the provided smart contract schema.<p>
 * Fields in the extending class, that should be serialized, must be visible to the serializer.
 * Visible fields are: <p>
 * <ul>
 * <li>Fields with 'getter' methods (either user-created or by @Getter annotation).</li>
 * <li>All fields in a class annotated with '@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)'. Fields can be excluded from this with the '@JsonIgnore' annotation.</li>
 * <li>Fields annotated with '@JsonProperty'. These have both getter and setter methods generated.</li>
 * <li>Public fields</li>
 * </ul>
 * If default serialization does not work, a custom serializer should be implemented and used with annotation '@JsonSerialize(using = MyCustomSerializer.class)'.
 * See {@link AbstractAddress} for an example of a custom serializer.
 * <p>
 * Parameter must be initialized by calling {@link SchemaParameter#initialize()} before usage.
 * <p>
 * For functions not expecting parameters use {@link Parameter#EMPTY} instead.
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
    @Getter(AccessLevel.NONE)
    private byte[] serializedParameter;

    /**
     * Creates a {@link SchemaParameter} of type {@link ParameterType#INIT} for use with {@link InitContractPayload}.
     * Parameter must be initialized with {@link SchemaParameter#initialize()} before use.
     *
     * @param schema   {@link Schema} of the module.
     * @param initName The name of the contract to be initialized with the parameter.
     */
    protected SchemaParameter(Schema schema, InitName initName) {
        this.schema = schema;
        this.receiveName = null;
        this.initName = initName;
        this.type = ParameterType.INIT;
        this.initialized = false;
    }

    /**
     * Creates a {@link SchemaParameter} of type {@link ParameterType#RECEIVE} for use with {@link UpdateContractPayload} and {@link InvokeInstanceRequest}.
     * Parameter must be initialized with {@link SchemaParameter#initialize()} before use.
     *
     * @param schema      {@link Schema} of the module.
     * @param receiveName {@link ReceiveName} containing the name of the contract and method to be updated/invoked with the parameter.
     */
    protected SchemaParameter(Schema schema, ReceiveName receiveName) {
        this.schema = schema;
        this.receiveName = receiveName;
        this.initName = null;
        this.type = ParameterType.RECEIVE;
        this.initialized = false;
    }


    /**
     * Initializes the parameter by ensuring serialization is performed correctly. This is required before the parameter is used.
     * Use {@link SchemaParameter#initialize(boolean)} with parameter 'true' for more descriptive errors.
     *
     * @throws CryptoJniException if the serialization could not be performed.
     */
    public void initialize() {
        initialize(false);
    }

    /**
     * Initializes the parameter by ensuring serialization is performed correctly. This is required before the parameter is used.
     *
     * @param verboseErrors whether to return errors in a verbose format or not. Defaults to false if omitted.
     * @throws CryptoJniException if the serialization could not be performed.
     */
    @SneakyThrows(org.apache.commons.codec.DecoderException.class)
    public void initialize(boolean verboseErrors) {
        byte[] schemaBytes = schema.getSchemaBytes();
        SchemaVersion schemaVersion = schema.getVersion();
        SerializeParameterResult result = getSerializeParameterResult(verboseErrors, schemaBytes, schemaVersion);
        if (!result.isSuccess()) {
            throw CryptoJniException.from(result.getErr());
        }
        serializedParameter = Hex.decodeHex(result.getSerializedParameter());
        this.initialized = true;
    }

    /**
     * Helper method for {@link SchemaParameter#initialize()}. Json serializes the parameter, performs the JNI call and deserializes the resulting json to {@link SerializeParameterResult}.
     *
     * @param verboseErrors whether to return verbose errors.
     * @param schemaBytes   byte[] containing the schema.
     * @param schemaVersion version of the schema.
     * @return {@link SerializeParameterResult} containing the serialized parameter if successful or a {@link JNIError} if not.
     */
    @SneakyThrows
    private SerializeParameterResult getSerializeParameterResult(boolean verboseErrors, byte[] schemaBytes, SchemaVersion schemaVersion) {
        SerializeParameterResult result;
        String parameterJson = JsonMapper.INSTANCE.writeValueAsString(this);
        String resultJson;
        resultJson = serializeUsingJNI(verboseErrors, schemaBytes, schemaVersion, parameterJson);
        result = JsonMapper.INSTANCE.readValue(resultJson, SerializeParameterResult.class);
        return result;
    }

    /**
     * Helper method performing the correct JNI call according to the present {@link ParameterType}.
     *
     * @param verboseErrors whether to return verbose errors.
     * @param schemaBytes   byte[] containing the schema.
     * @param schemaVersion version of the schema.
     * @param parameterJson the parameter serialized as json.
     * @return json representing a {@link SerializeParameterResult}.
     */
    private String serializeUsingJNI(boolean verboseErrors, byte[] schemaBytes, SchemaVersion schemaVersion, String parameterJson) {
        String resultJson;
        if (this.type == ParameterType.INIT) {
            String contractName = initName.getName();
            resultJson = CryptoJniNative.serializeInitParameter(parameterJson, contractName, schemaBytes, schemaVersion.getVersion(), verboseErrors);
        } else {
            String contractName = receiveName.getContractName();
            String methodName = receiveName.getMethod();
            resultJson = CryptoJniNative.serializeReceiveParameter(parameterJson, contractName, methodName, schemaBytes, schemaVersion.getVersion(), verboseErrors);
        }
        return resultJson;
    }

    /**
     * Converts the serialized parameter to a byte[]. Should only be called after parameter is initialized with {@link SchemaParameter#initialize()}.
     *
     * @return byte[] containing the serialized parameter.
     * @throws IllegalStateException if parameter is not initialized.
     */
    public byte[] toBytes() {
        if (!initialized) {
            throw new IllegalStateException("Must initialize parameter before use");
        }
        return serializedParameter;
    }
}
