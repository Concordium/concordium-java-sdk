package com.concordium.sdk.transactions;

import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;

@ToString
@Getter
public final class InitContractPayload {

    private final CCDAmount amount;
    private final Hash moduleRef;
    private final InitName initName;
    private final Parameter param;

    private InitContractPayload(CCDAmount amount, Hash moduleRef, InitName initName, Parameter param) {
        this.amount = amount;
        this.moduleRef = moduleRef;
        this.initName = initName;
        this.param = param;
    }

    public static InitContractPayload from(int amount, byte[] moduleRef, String contractName, byte[] parameter) {
        return new InitContractPayload(
                CCDAmount.fromMicro(amount),
                Hash.from(moduleRef),
                InitName.from(contractName),
                Parameter.from(parameter)
        );
    }

    public byte[] getBytes() {
        val amountBytes = amount.getBytes();
        val moduleRefBytes = moduleRef.getBytes();
        val initNameBytes = initName.getBytes();
        val param_bytes = param.getBytes();
        val bufferLength = TransactionType.BYTES +
                moduleRefBytes.length +
                amountBytes.length +
                initNameBytes.length +
                param_bytes.length;

        val buffer = ByteBuffer.allocate(bufferLength);

        buffer.put(TransactionType.INITIALIZE_SMART_CONTRACT_INSTANCE.getValue());
        buffer.put(amountBytes);
        buffer.put(moduleRefBytes);
        buffer.put(initNameBytes);
        buffer.put(param_bytes);

        return buffer.array();
    }
}
