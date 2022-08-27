package com.concordium.sdk.transactions;

import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;

@ToString
@Getter
public final class InitContractPayload {

    private final CCDAmount amount;
    private final Hash mod_ref;
    private final InitName init_name;
    private final Parameter param;

    private InitContractPayload(CCDAmount amount, Hash mod_ref, InitName init_name, Parameter param) {
        this.amount = amount;
        this.mod_ref = mod_ref;
        this.init_name = init_name;
        this.param = param;
    }

    public static InitContractPayload from(int amount, byte[] mod_ref, String contract_name, byte[] parameter) {
        return new InitContractPayload(
                CCDAmount.fromMicro(amount),
                Hash.from(mod_ref),
                InitName.from(contract_name),
                Parameter.from(parameter)
        );
    }

    public byte[] getBytes() {
        val amount_bytes = amount.getBytes();
        val mod_ref_bytes = mod_ref.getBytes();
        val init_name_bytes = init_name.getBytes();
        val param_bytes = param.getBytes();
        val buffer = ByteBuffer.allocate(TransactionType.BYTES + mod_ref_bytes.length + amount_bytes.length + init_name_bytes.length + param_bytes.length);
        buffer.put(TransactionType.INITIALIZE_SMART_CONTRACT_INSTANCE.getValue());
        buffer.put(amount_bytes);
        buffer.put(mod_ref_bytes);
        buffer.put(init_name_bytes);
        buffer.put(param_bytes);
        return buffer.array();
    }
}
