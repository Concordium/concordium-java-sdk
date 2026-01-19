package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.bakertransactions.BakerKeys;
import com.concordium.sdk.crypto.bakertransactions.ConfigureBakerKeys;
import com.concordium.sdk.crypto.bakertransactions.ConfigureBakerKeysJniInput;
import com.concordium.sdk.crypto.bakertransactions.ConfigureBakerKeysJniOutput;
import com.concordium.sdk.types.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Hex;

/**
 * Baker keys with proofs.
 */
@Getter
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ConfigureBakerKeysPayload {
    private final byte[] bytes;

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
    public static ConfigureBakerKeysPayload getNewConfigureBakerKeysPayload(AccountAddress sender, BakerKeys bakerKeys) {
        // Create the input for the ConfigureBakerKeys JNI function
        ConfigureBakerKeysJniInput input = ConfigureBakerKeysJniInput.builder()
                .keys(bakerKeys)
                .sender(sender)
                .build();

        // Call the JNI function to generate the configure baker keys payload
        ConfigureBakerKeysJniOutput output = ConfigureBakerKeys.generateConfigureBakerKeysPayload(input);
        // Create a ConfigureBakerKeysPayload object with the JNI output
        return ConfigureBakerKeysPayload.from(output);
    }

    @Override
    @JsonValue
    public String toString() {
        return Hex.encodeHexString(this.bytes);
    }

}
