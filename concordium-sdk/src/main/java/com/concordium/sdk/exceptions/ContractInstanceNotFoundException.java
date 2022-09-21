package com.concordium.sdk.exceptions;

import com.concordium.sdk.responses.transactionstatus.ContractAddress;
import com.concordium.sdk.transactions.Hash;
import lombok.Getter;

/**
 * A checked exception thrown when a contract instance could not be found with the {@link ContractAddress}
 * for the block {@link Hash}.
 */
public class ContractInstanceNotFoundException extends Exception {
    private ContractInstanceNotFoundException(ContractAddress contractAddress, Hash blockHash) {
        super(String.format("Contract Instance <index: %d, subIndex: %d> not found for block %s",
                contractAddress.getIndex(),
                contractAddress.getSubIndex(),
                blockHash.asHex()));
    }

    public static ContractInstanceNotFoundException from(ContractAddress contractAddress, Hash blockHash) {
        return new ContractInstanceNotFoundException(contractAddress, blockHash);
    }
}
