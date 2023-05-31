package com.concordium.sdk.responses.transactionevent.accounttransactionresults;


import com.concordium.sdk.responses.transactionstatus.TransactionResultEvent;

/**
 * Effects produced by successful smart contract invocations.
 * A single invocation will produce a sequence of effects.
 */
public interface ContractTraceElement extends TransactionResultEvent {

}
