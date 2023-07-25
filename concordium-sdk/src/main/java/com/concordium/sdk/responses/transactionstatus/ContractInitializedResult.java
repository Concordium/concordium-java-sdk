package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.ContractAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

/**
 * A contract was initialized
 */
@Getter
@ToString
public final class ContractInitializedResult extends TransactionResultEvent {

    /**
     * Module in which the contract source resides.
     */
    private final String ref;

    /**
     * Address of the contract deployed.
     */
    private final ContractAddress address;

    /**
     * Initial amount transferred to the contract.
     */
    private CCDAmount amount;

    /**
     * Name of the contract init function being called.
     */
    private final String initName;

    /**
     * Events as reported by the contract via the log method, in the
     * order they were reported.
     */
    private final List<String> events;

    /**
     * The contract version of the contract that was initialized.
     */
    private final int version;

    @JsonCreator
    ContractInitializedResult(@JsonProperty("ref") String ref,
                              @JsonProperty("address") ContractAddress address,
                              @JsonProperty("amount") String amount,
                              @JsonProperty("initName") String initName,
                              @JsonProperty("events") List<String> events,
                              @JsonProperty("contractVersion") int version) {
        this.ref = ref;
        this.address = address;
        if (!Objects.isNull(amount)) {
            this.amount = CCDAmount.fromMicro(amount);
        }
        this.initName = initName;
        this.events = events;
        this.version = version;
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.CONTRACT_INITIALIZED;
    }
}