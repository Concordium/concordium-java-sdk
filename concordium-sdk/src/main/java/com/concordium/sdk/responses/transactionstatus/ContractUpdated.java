package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.InstanceUpdatedEvent;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.AbstractAddress;
import com.concordium.sdk.types.ContractAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import lombok.*;
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
@AllArgsConstructor
@Builder
public class ContractUpdated implements TransactionResultEvent {

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

    /**
     * Parses {@link InstanceUpdatedEvent} to {@link ContractUpdated}.
     * @param contractUpdateIssued {@link InstanceUpdatedEvent} returned by the GRPC V2 API.
     * @return parsed {@link ContractUpdated}.
     */
    public static ContractUpdated parse(InstanceUpdatedEvent contractUpdateIssued) {
        val events = new ImmutableList.Builder<byte[]>();
        contractUpdateIssued.getEventsList().forEach(e -> events.add(e.getValue().toByteArray()));
        return ContractUpdated.builder()
                .version(ContractVersion.forValue(contractUpdateIssued.getContractVersionValue()))
                .address(ContractAddress.parse(contractUpdateIssued.getAddress()))
                .instigator(AbstractAddress.parse(contractUpdateIssued.getInstigator()))
                .amount(CCDAmount.fromMicro(contractUpdateIssued.getAmount().getValue()))
                .message(Hex.encodeHexString(contractUpdateIssued.getParameter().getValue().toByteArray()))
                .receiveName(contractUpdateIssued.getReceiveName().getValue())
                .events(events.build())
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.CONTRACT_UPDATED;
    }
}
