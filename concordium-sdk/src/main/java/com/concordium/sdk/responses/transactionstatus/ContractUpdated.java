package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A contract was updated
 */
@ToString
@Getter
public class ContractUpdated extends TransactionResultEvent {

    /**
     * The amount provided
     */
    private CCDAmount amount;

    /**
     * The instigator i.e. the source invoking the contract.
     */
    private final AbstractAccount instigator;

    /**
     * The address of the contract
     */
    private final ContractAddress address;

    /**
     * The name of the `receive` function which was called.
     */
    private final String receiveName;

    /**
     * Events as reported by the contract via the log method, in the
     * order they were reported.
     */
    private final List<String> events;

    /**
     * The message which was sent to the contract.
     */
    private final String message;

    /**
     * The contract version
     */
    private final int version;

    @JsonCreator
    ContractUpdated(@JsonProperty("amount") String amount,
                    @JsonProperty("instigator") Map<String, Object> instigator,
                    @JsonProperty("address") Map<String, Object> address,
                    @JsonProperty("receiveName") String receiveName,
                    @JsonProperty("events") List<String> events,
                    @JsonProperty("message") String message,
                    @JsonProperty("contractVersion") int version) {
        this.instigator = AbstractAccount.parseAccount(instigator);
        this.address = (ContractAddress) AbstractAccount.parseAccount(address);
        this.receiveName = receiveName;
        this.events = events;
        this.message = message;
        if (!Objects.isNull(amount)) {
            this.amount = CCDAmount.fromMicro(amount);
        }
        this.version = version;
    }
}
