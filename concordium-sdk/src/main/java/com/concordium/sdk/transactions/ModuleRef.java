package com.concordium.sdk.transactions;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;


@Getter
@ToString
public final class ModuleRef {

    private final String module_ref;

    @JsonCreator
    ModuleRef(String module_ref) {
        this.module_ref = module_ref;
    }

    public static ModuleRef from(String module_ref) {
        return new ModuleRef(module_ref);
    }

    public byte[] getBytes() {
        val mod_ref_bytes = this.module_ref.getBytes();
        val buffer = ByteBuffer.allocate(mod_ref_bytes.length);
        buffer.put(mod_ref_bytes);
        return buffer.array();
    }
}

