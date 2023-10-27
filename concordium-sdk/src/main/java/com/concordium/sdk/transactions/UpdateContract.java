package com.concordium.sdk.transactions;

import com.concordium.sdk.transactions.smartcontracts.ParameterType;
import com.concordium.sdk.transactions.smartcontracts.SchemaParameter;
import com.concordium.sdk.types.ContractAddress;
import lombok.*;

import java.nio.ByteBuffer;

/**
 * The payload for updating a smart contract.
 */
@ToString
@Getter
@EqualsAndHashCode(callSuper = true)
public final class UpdateContract extends Payload {
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


    private UpdateContract(@NonNull final CCDAmount amount,
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
    public static UpdateContract from(final long amount,
                                      @NonNull final ContractAddress contractAddress,
                                      @NonNull final String contractName,
                                      @NonNull final String method,
                                      final byte @NonNull [] parameter) {
        return from(CCDAmount.fromMicro(amount),
                contractAddress,
                ReceiveName.from(contractName, method),
                Parameter.from(parameter));
    }

    /**
     * Creates a {@link UpdateContract} from the given parameters.
     *
     * @param amount          The amount of CCD to be sent to the contract.
     * @param contractAddress Address of the contract instance to invoke.
     * @param receiveName The {@link ReceiveName} of the smart contract instance to invoke.
     * @param param       The parameter of the contract method.
     * @return A new UpdateContractPayload object.
     */
    public static UpdateContract from(@NonNull final CCDAmount amount,
                                      @NonNull final ContractAddress contractAddress,
                                      @NonNull final ReceiveName receiveName,
                                      @NonNull final Parameter param) {
        return new UpdateContract(amount, contractAddress, receiveName, param);
    }

    /**
     * Creates a {@link UpdateContract} with amount = 0 from the given parameters.
     *
     * @param contractAddress Address of the contract instance to invoke.
     * @param schemaParameter {@link SchemaParameter} message to invoke the contract with. Must be initialized with {@link SchemaParameter#initialize()} beforehand.
     */
    public static UpdateContract from(@NonNull final ContractAddress contractAddress,
                                      SchemaParameter schemaParameter) {
        if (!(schemaParameter.getType() == ParameterType.RECEIVE)) {
            throw new IllegalArgumentException("SchemaParameter for UpdateContractPayload must be initialized with a ReceiveName");
        }
        return from(CCDAmount.fromMicro(0),
                contractAddress,
                schemaParameter.getReceiveName(),
                Parameter.from(schemaParameter));
    }

    /**
     * Creates a {@link UpdateContract} from the given parameters.
     *
     * @param amount          The amount of CCD to be sent to the contract.
     * @param contractAddress Address of the contract instance to invoke.
     * @param schemaParameter {@link SchemaParameter} message to invoke the contract with. Must be initialized with {@link SchemaParameter#initialize()} beforehand.
     */
    public static UpdateContract from(CCDAmount amount,
                                      @NonNull final ContractAddress contractAddress,
                                      SchemaParameter schemaParameter) {
        if (!(schemaParameter.getType() == ParameterType.RECEIVE)) {
            throw new IllegalArgumentException("SchemaParameter for UpdateContractPayload must be initialized with a ReceiveName");
        }
        return from(amount, contractAddress, schemaParameter.getReceiveName(), Parameter.from(schemaParameter));
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.UPDATE_SMART_CONTRACT_INSTANCE;
    }

    @Override
    public byte[] getRawPayloadBytes() {
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
