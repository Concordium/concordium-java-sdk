package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.bakertransactions.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.codec.binary.Hex;

@Getter
@EqualsAndHashCode
public class ConfigureBakerKeysPayload {
    private final byte[] bytes;

    private ConfigureBakerKeysPayload(byte[] bytes) {this.bytes = bytes;}

    @JsonCreator
    public static ConfigureBakerKeysPayload from(ConfigureBakerKeysJniOutput jniOutput) {
        return new ConfigureBakerKeysPayload(jniOutput.getBytes());
    }

    public static ConfigureBakerKeysPayload getNewConfigureBakerKeysPayload(AccountAddress sender) {
        BakerKeysJniOutput bakerKeys = BakerKeys.createBakerKeys();
        ConfigureBakerKeysJniInput input = ConfigureBakerKeysJniInput.builder()
                .keys(bakerKeys)
                .sender(sender)
                .build();

        ConfigureBakerKeysJniOutput output = ConfigureBakerKeys.generateConfigureBakerKeysPayload(input);
        ConfigureBakerKeysPayload keysWithProofs = ConfigureBakerKeysPayload.from(output);
        return keysWithProofs;
    }

    @Override
    @JsonValue
    public String toString() {
        return Hex.encodeHexString(this.bytes);
    }

}
