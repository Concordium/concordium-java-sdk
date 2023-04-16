package com.concordium.sdk.transactions;

import com.concordium.sdk.types.ContractAddress;
import com.google.common.primitives.Bytes;
import lombok.*;

import java.nio.ByteBuffer;
import java.util.stream.Stream;

import static com.google.common.primitives.Bytes.concat;

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

    private UpdateContractPayload(@NonNull final CCDAmount amount,
                                  @NonNull final ContractAddress contractAddress,
                                  @NonNull final ReceiveName receiveName,
                                  @NonNull final Parameter param) {
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
    public static UpdateContractPayload from(final long amount,
                                             @NonNull final ContractAddress contractAddress,
                                             @NonNull final String contractName,
                                             @NonNull final String method,
                                             final byte @NonNull [] parameter) {
        return from(CCDAmount.fromMicro(amount),
                contractAddress,
                ReceiveName.from(contractName, method),
                Parameter.from(parameter));
    }

    public static UpdateContractPayload from(@NonNull final CCDAmount amount,
                                             @NonNull final ContractAddress contractAddress,
                                             @NonNull final ReceiveName receiveName,
                                             @NonNull final Parameter param) {
        return new UpdateContractPayload(amount, contractAddress, receiveName, param);
    }

    public byte[] getBytes() {
        val amountBytes = amount.getBytes();
        val contractAddressBytes = contractAddress.getBytes();
        val receiveNameBytes = receiveName.getBytes();
        val paramBytes = param.getBytes();

        val buffer = ByteBuffer.allocate(contractAddressBytes.length
                + amountBytes.length
                + receiveNameBytes.length
                + paramBytes.length);
        buffer.put(amountBytes);
        buffer.put(contractAddressBytes);
        buffer.put(receiveNameBytes);
        buffer.put(paramBytes);

        return buffer.array();
    }
}
