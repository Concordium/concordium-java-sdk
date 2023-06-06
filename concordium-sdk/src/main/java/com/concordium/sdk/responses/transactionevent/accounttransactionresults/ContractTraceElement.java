package com.concordium.sdk.responses.transactionevent.accounttransactionresults;


import com.concordium.sdk.responses.transactionstatus.*;

/**
 * Effects produced by successful smart contract invocations.
 * A single invocation will produce a sequence of effects.
 */
public interface ContractTraceElement {

    ContractTraceElementType getTraceType();

    /**
     * Parses {@link com.concordium.grpc.v2.ContractTraceElement} to {@link ContractTraceElement}.
     * @param traceElement {@link com.concordium.grpc.v2.ContractTraceElement} returned by the GRPC V2 API.
     * @return parsed {@link ContractTraceElement}.
     */
    static ContractTraceElement parse(com.concordium.grpc.v2.ContractTraceElement traceElement) {
        switch (traceElement.getElementCase()) {
            case UPDATED:
                return ContractUpdated.parse(traceElement.getUpdated());
            case TRANSFERRED:
                TransferredResult.parse(traceElement.getTransferred());
            case INTERRUPTED:
                return InterruptedResult.parse(traceElement.getInterrupted());
            case RESUMED:
                return ResumedResult.parse(traceElement.getResumed());
            case UPGRADED:
                return UpgradedResult.parse(traceElement.getUpgraded());
            default:
                throw new IllegalArgumentException();
        }
    }
}
