package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.bakertransactions.AddBakerKeysJniOutput;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.codec.binary.Hex;

@Getter
@EqualsAndHashCode
public class AddBakerKeysPayload {
    private final byte[] bytes;

    private AddBakerKeysPayload(byte[] bytes) {this.bytes = bytes;}

    @JsonCreator
    public static AddBakerKeysPayload from(AddBakerKeysJniOutput jniOutput) {
        return new AddBakerKeysPayload(jniOutput.getBytes());
    }

    @Override
    @JsonValue
    public String toString() {
        return Hex.encodeHexString(this.bytes);
    }

}
