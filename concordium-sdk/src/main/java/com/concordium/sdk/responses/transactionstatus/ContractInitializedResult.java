package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.ContractInitializedEvent;
import com.concordium.grpc.v2.ContractVersion;
import com.concordium.sdk.responses.transactionevent.accounttransactionresults.AccountTransactionResult;
import com.concordium.sdk.responses.transactionevent.accounttransactionresults.TransactionType;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.ContractAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import lombok.*;
import org.apache.commons.codec.binary.Hex;

import java.util.List;
import java.util.Objects;

/**
 * A contract was initialized
 */
@Getter
@ToString
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
public final class ContractInitializedResult extends TransactionResultEvent implements AccountTransactionResult {

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

    /**
     * Parses {@link ContractInitializedEvent} to {@link ContractInitializedResult}.
     *
     * @param contractInitialized {@link ContractInitializedEvent} returned by the GRPC V2 API.
     * @return parsed {@link ContractInitializedResult}.
     */
    public static ContractInitializedResult parse(ContractInitializedEvent contractInitialized) {
        val events = new ImmutableList.Builder<String>();
        int version;
        if (contractInitialized.getContractVersion() == ContractVersion.V0) {
            version = 0;
        } else {
            version = 1;
        }
        contractInitialized.getEventsList().forEach(e -> events.add(Hex.encodeHexString(e.getValue().toByteArray())));
        return ContractInitializedResult.builder()
                .version(version)
                .ref(Hex.encodeHexString(contractInitialized.getOriginRef().getValue().toByteArray()))
                .address(ContractAddress.parse(contractInitialized.getAddress()))
                .amount(CCDAmount.fromMicro(contractInitialized.getAmount().getValue()))
                .initName(contractInitialized.getInitName().getValue())
                .events(events.build())
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.CONTRACT_INITIALIZED;
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.INIT_CONTRACT;
    }
}