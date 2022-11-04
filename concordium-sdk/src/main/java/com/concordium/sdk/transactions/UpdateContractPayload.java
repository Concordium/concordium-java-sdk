package com.concordium.sdk.transactions;

import com.concordium.sdk.responses.transactionstatus.ContractAddress;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;

/**
 * Data needed to update a smart contract.
 */
@ToString
@Getter
public final class UpdateContractPayload {
    /**
     * Send the given amount of CCD.
     */
    private final CCDAmount amount;
    /**
     * Address of the contract instance to invoke.
     */
    private final ContractAddress contract_address;
    /**
     * Name of the method to invoke on the contract.
     */
    private final ReceiveName receive_name;
    /**
     * Message to send to the contract instance
     */
    private final Parameter param;

    public UpdateContractPayload(CCDAmount amount, ContractAddress contract_address, ReceiveName receive_name, Parameter param) {
        this.amount = amount;
        this.contract_address = contract_address;
        this.receive_name = receive_name;
        this.param = param;
    }

    public static UpdateContractPayload from(int amount, ContractAddress account_address, String contract_name, String method, byte[] parameter) {
        return new UpdateContractPayload(
                CCDAmount.fromMicro(amount),
                account_address,
                ReceiveName.from(contract_name, method),
                Parameter.from(parameter)
        );
    }

    public byte[] getBytes() {
        val amount_bytes = amount.getBytes();
        val contract_address_bytes = contract_address.getBytes();
        val receive_name_bytes = receive_name.getBytes();
        val param_bytes = param.getBytes();
        val buffer = ByteBuffer.allocate(TransactionType.BYTES + contract_address_bytes.length + amount_bytes.length + receive_name_bytes.length + param_bytes.length);
        buffer.put(TransactionType.UPDATE_SMART_CONTRACT_INSTANCE.getValue());
        buffer.put(amount_bytes);
        buffer.put(contract_address_bytes);
        buffer.put(receive_name_bytes);
        buffer.put(param_bytes);
        return buffer.array();
    }
}
