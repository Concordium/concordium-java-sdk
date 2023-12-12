package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.ContractEvent;
import com.concordium.grpc.v2.InstanceUpdatedEvent;
import com.concordium.sdk.responses.smartcontracts.ContractTraceElement;
import com.concordium.sdk.responses.smartcontracts.ContractTraceElementType;
import com.concordium.sdk.responses.smartcontracts.ContractVersion;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.AbstractAddress;
import com.concordium.sdk.types.ContractAddress;
import com.google.protobuf.ByteString;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A contract was updated
 */
@ToString
@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class ContractUpdated implements TransactionResultEvent, ContractTraceElement {

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
    private final byte[] message;

    /**
     * The contract version.
     */
    private final ContractVersion version;
    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.CONTRACT_UPDATED;
    }

    @Override
    public ContractTraceElementType getTraceType() {
        return ContractTraceElementType.INSTANCE_UPDATED;
    }

    public static ContractUpdated from(InstanceUpdatedEvent e) {
        val events = e.getEventsList()
                .stream()
                .map(ContractEvent::getValue)
                .map(ByteString::toByteArray)
                .collect(Collectors.toList());
        return ContractUpdated
                .builder()
                .amount(CCDAmount.from(e.getAmount()))
                .instigator(AbstractAddress.from(e.getInstigator()))
                .address(ContractAddress.from(e.getAddress()))
                .receiveName(e.getReceiveName().getValue())
                .message(e.getParameter().getValue().toByteArray())
                .version(ContractVersion.from(e.getContractVersion()))
                .events(events)
                .build();
    }
}
