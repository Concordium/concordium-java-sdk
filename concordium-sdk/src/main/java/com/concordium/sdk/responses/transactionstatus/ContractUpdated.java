package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.AbstractAddress;
import com.concordium.sdk.types.ContractAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;
import org.apache.commons.codec.binary.Hex;

import java.util.ArrayList;
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
     * This can either be account or a contract.
     */
    private final AbstractAddress instigator;

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
    private final List<byte[]> events;

    /**
     * The message which was sent to the contract.
     */
    private final String message;

    /**
     * The contract version.
     */
    private final ContractVersion version;

    @SneakyThrows
    @JsonCreator
    ContractUpdated(@JsonProperty("amount") String amount,
                    @JsonProperty("instigator") Map<String, Object> instigator,
                    @JsonProperty("address") Map<String, Object> address,
                    @JsonProperty("receiveName") String receiveName,
                    @JsonProperty("events") List<String> events,
                    @JsonProperty("message") String message,
                    @JsonProperty("contractVersion") ContractVersion version) {
        this.instigator = AbstractAddress.parseAccount(instigator);
        this.address = (ContractAddress) AbstractAddress.parseAccount(address);
        this.receiveName = receiveName;
        this.events = new ArrayList<>();
        for (String event : events) {
            this.events.add(Hex.decodeHex(event));
        }
        this.message = message;
        if (!Objects.isNull(amount)) {
            this.amount = CCDAmount.fromMicro(amount);
        }
        this.version = version;
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.CONTRACT_UPDATED;
    }
}
