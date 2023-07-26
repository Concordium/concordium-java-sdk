package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.bakertransactions.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.codec.binary.Hex;

/**
 * The payload of ConfigureBakerKeysPayload
 */
@Getter
@EqualsAndHashCode
public class ConfigureBakerKeysPayload {
    private final byte[] bytes;

    private ConfigureBakerKeysPayload(byte[] bytes) {
        this.bytes = bytes;
    }

    @JsonCreator
    public static ConfigureBakerKeysPayload from(ConfigureBakerKeysJniOutput jniOutput) {
        return new ConfigureBakerKeysPayload(jniOutput.getBytes());
    }

    /**
     * Generates a new ConfigureBakerKeysPayload with the given sender and baker keys
     *
     * @param sender The account address of the sender
     * @return The newly generated ConfigureBakerKeysPayload
     */
    public static ConfigureBakerKeysPayload getNewConfigureBakerKeysPayload(AccountAddress sender) {
        // Create a new set of baker keys
        BakerKeysJniOutput bakerKeys = BakerKeys.createBakerKeys();
        // Create the input for the ConfigureBakerKeys JNI function
        ConfigureBakerKeysJniInput input = ConfigureBakerKeysJniInput.builder()
                .keys(bakerKeys)
                .sender(sender)
                .build();

        // Call the JNI function to generate the configure baker keys payload
        ConfigureBakerKeysJniOutput output = ConfigureBakerKeys.generateConfigureBakerKeysPayload(input);
        // Create a ConfigureBakerKeysPayload object with the JNI output
        ConfigureBakerKeysPayload keysWithProofs = ConfigureBakerKeysPayload.from(output);
        return keysWithProofs;
    }

    @Override
    @JsonValue
    public String toString() {
        return Hex.encodeHexString(this.bytes);
    }

}
