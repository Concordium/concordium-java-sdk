package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.ContractEvent;
import com.concordium.grpc.v2.ContractInitializedEvent;
import com.concordium.sdk.responses.modulelist.ModuleRef;
import com.concordium.sdk.responses.smartcontracts.ContractVersion;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.ContractAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.protobuf.ByteString;
import lombok.*;
import org.apache.commons.codec.binary.Hex;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A contract was initialized
 */
@Getter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public final class ContractInitializedResult implements TransactionResultEvent {

    /**
     * Module in which the contract source resides.
     */
    private final ModuleRef ref;

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
     * Events (hex encoded) as reported by the contract via the log method, in the
     * order they were reported.
     */
    private final List<byte[]> events;

    /**
     * The contract version of the contract that was initialized.
     */
    private final ContractVersion version;

    @JsonCreator
    @SneakyThrows
    ContractInitializedResult(@JsonProperty("ref") ModuleRef modRef,
                              @JsonProperty("address") ContractAddress address,
                              @JsonProperty("amount") String amount,
                              @JsonProperty("initName") String initName,
                              @JsonProperty("events") List<String> events,
                              @JsonProperty("contractVersion") ContractVersion version) {
        this.ref = modRef;
        this.address = address;
        if (!Objects.isNull(amount)) {
            this.amount = CCDAmount.fromMicro(amount);
        }
        this.initName = initName;
        val list = new ArrayList<byte[]>();
        for (String event : events) {
            list.add(Hex.decodeHex(event));
        }
        this.events = list;
        this.version = version;
    }

    public static ContractInitializedResult from(ContractInitializedEvent contractInitialized) {
        val events = contractInitialized.getEventsList()
                .stream()
                .map(ContractEvent::getValue)
                .map(ByteString::toByteArray)
                .collect(Collectors.toList());
        return ContractInitializedResult
                .builder()
                .address(ContractAddress.from(contractInitialized.getAddress()))
                .ref(ModuleRef.from(contractInitialized.getOriginRef()))
                .amount(CCDAmount.from(contractInitialized.getAmount()))
                .version(ContractVersion.from(contractInitialized.getContractVersion()))
                .initName(contractInitialized.getInitName().getValue())
                .events(events)
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.CONTRACT_INITIALIZED;
    }
}