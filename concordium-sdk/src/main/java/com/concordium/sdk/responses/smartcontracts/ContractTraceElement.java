package com.concordium.sdk.responses.smartcontracts;

import com.concordium.sdk.responses.transactionstatus.*;

/**
 * Effects produced by successful smart contract invocations.
 * A single invocation will produce a sequence of effects.
 */
public interface ContractTraceElement {
    ContractTraceElementType getTraceType();

    static ContractTraceElement from(com.concordium.grpc.v2.ContractTraceElement contractTraceElement) {
        switch (contractTraceElement.getElementCase()) {
            case UPDATED:
                return ContractUpdated.from(contractTraceElement.getUpdated());
            case TRANSFERRED:
                return TransferredResult.from(contractTraceElement.getTransferred());
            case INTERRUPTED:
                return InterruptedResult.from(contractTraceElement.getInterrupted());
            case RESUMED:
                return ResumedResult.from(contractTraceElement.getResumed());
            case UPGRADED:
                return UpgradedResult.from(contractTraceElement.getUpgraded());
            case ELEMENT_NOT_SET:
                throw new IllegalArgumentException("Trace element type not set.");
        }
        throw new IllegalArgumentException("Unrecognized trace element.");
    }
}
