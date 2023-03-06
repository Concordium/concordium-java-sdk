package com.concordium.sdk.transactions;

import com.concordium.sdk.types.ContractAddress;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;

/**
 * The payload for updating a smart contract.
 */
@ToString
@Getter
@EqualsAndHashCode
public final class UpdateContractPayload {
    /**
     * Send the given amount of CCD to the smart contract.
     */
    private final CCDAmount amount;
    /**
     * Address of the contract instance to invoke.
     */
    private final ContractAddress contractAddress;
    /**
     * Name of the method to invoke on the contract.
     */
    private final ReceiveName receiveName;
    /**
     * Message to send to the contract instance
     */
    private final Parameter param;

    private UpdateContractPayload(CCDAmount amount,
                                  ContractAddress contractAddress,
                                  ReceiveName receiveName,
                                  Parameter param) {
        this.amount = amount;
        this.contractAddress = contractAddress;
        this.receiveName = receiveName;
        this.param = param;
    }

    /**
     * > This function creates a payload for updating a contract
     *
     * @param amount          The amount of CCD to be sent to the contract.
     * @param contractAddress Address of the contract instance to invoke.
     * @param contractName    Name of the contract to update.
     * @param method          Name of the method to invoke on the contract
     * @param parameter       The parameter of the contract method.
     * @return A new UpdateContractPayload object.
     */
    public static UpdateContractPayload from(int amount,
                                             ContractAddress contractAddress,
                                             String contractName,
                                             String method,
                                             byte[] parameter) {
        return from(CCDAmount.fromMicro(amount),
                contractAddress,
                ReceiveName.from(contractName, method),
                Parameter.from(parameter));
    }

    public static UpdateContractPayload from(CCDAmount amount,
                                      ContractAddress contractAddress,
                                      ReceiveName receiveName,
                                      Parameter param) {
        return new UpdateContractPayload(amount, contractAddress, receiveName, param);
    }

    public byte[] getBytes() {
        val amount_bytes = amount.getBytes();
        val contract_address_bytes = contractAddress.getBytes();
        val receive_name_bytes = receiveName.getBytes();
        val param_bytes = param.getBytes();
        val buffer = ByteBuffer.allocate(contract_address_bytes.length + amount_bytes.length + receive_name_bytes.length + param_bytes.length);
        buffer.put(amount_bytes);
        buffer.put(contract_address_bytes);
        buffer.put(receive_name_bytes);
        buffer.put(param_bytes);

        return buffer.array();
    }
}
