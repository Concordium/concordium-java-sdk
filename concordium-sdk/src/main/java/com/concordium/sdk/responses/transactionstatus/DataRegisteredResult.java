package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.RegisteredData;
import com.concordium.sdk.responses.transactionevent.accounttransactionresults.AccountTransactionResult;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.codec.binary.Hex;

/**
 * Some data was registered on the chain.
 */
@Getter
@ToString
@Builder
@EqualsAndHashCode
public final class DataRegisteredResult implements TransactionResultEvent, AccountTransactionResult {

    /**
     * Data registered.
     */
    private final String data;

    @JsonCreator
    DataRegisteredResult(@JsonProperty("data") String data) {
        this.data = data;
    }


    /**
     * Parses {@link RegisteredData} to {@link DataRegisteredResult}.
     * @param dataRegistered {@link RegisteredData} returned by the GRPC V2 API.
     * @return parsed {@link DataRegisteredResult}.
     */
    public static DataRegisteredResult parse(RegisteredData dataRegistered) {
        return DataRegisteredResult.builder()
                .data(Hex.encodeHexString(dataRegistered.getValue().toByteArray()))
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.DATA_REGISTERED;
    }
}
