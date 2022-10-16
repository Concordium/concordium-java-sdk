package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt16;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;


/**
 * A contract name (owned version). Expected format: "init_<contract_name>"
 * A contract name must only contain ascii alphanumeric or punctuation (but not a '.')
 * Max length of the contract name can be 100
 */
@Getter
@ToString
public final class InitName {
    private final String name;

    private final int MAX_FUNC_NAME_SIZE = 100;

    @JsonCreator
    InitName(String name) {
        if (!validateInitName(name)) {
            throw new IllegalArgumentException("Invalid contract name provided");
        }
        this.name = name;
    }

    private boolean validateInitName(String name) {
        if(name == null){
            return false;
        }
        if(!name.startsWith("init_")){
            return false;
        }
        if(name.length() > MAX_FUNC_NAME_SIZE){
            return false;
        }
        if(name.contains(".")){
            return false;
        }
        if(!name.matches("^[!-/:-@\\[-`{~a-zA-Z0-9]*$")){
            return false;
        }
        return true;
    }

    public static InitName from(String name) {
        return new InitName(name);
    }

    public byte[] getBytes() {
        val initNameBuffer = name.getBytes();
        val buffer = ByteBuffer.allocate(UInt16.BYTES + initNameBuffer.length);
        buffer.put(UInt16.from(initNameBuffer.length).getBytes());
        buffer.put(initNameBuffer);
        return buffer.array();
    }
}
