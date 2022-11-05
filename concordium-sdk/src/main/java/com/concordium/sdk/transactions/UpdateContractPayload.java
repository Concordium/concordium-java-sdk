package com.concordium.sdk.transactions;

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

    // A constructor.
    UpdateContractPayload(CCDAmount amount, ContractAddress contract_address, ReceiveName receive_name, Parameter param) {
        this.amount = amount;
        this.contract_address = contract_address;
        this.receive_name = receive_name;
        this.param = param;
    }

    /**
     * > This function creates a payload for updating a contract
     *
     * @param amount           The amount of CCD to be sent to the contract.
     * @param contract_address Address of the contract instance to invoke.
     * @param contract_name    Name of the contract to update.
     * @param method           Name of the method to invoke on the contract
     * @param parameter        The parameter of the contract method.
     * @return A new UpdateContractPayload object.
     */
    public static UpdateContractPayload from(int amount, ContractAddress contract_address, String contract_name, String method, byte[] parameter) {
        return new UpdateContractPayload(
                CCDAmount.fromMicro(amount),
                contract_address,
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
