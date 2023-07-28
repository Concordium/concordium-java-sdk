package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.RegisteredData;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.codec.binary.Hex;

/**
 * Result of data being registered on chain.
 */
@Getter
@ToString
@EqualsAndHashCode
@Builder
public final class DataRegisteredResult implements TransactionResultEvent {

    /**
     * The hex encoded data registered on the chain.
     */
    private final String data;

    @JsonCreator
    DataRegisteredResult(@JsonProperty("data") String data) {
        this.data = data;
    }

    public static DataRegisteredResult from(RegisteredData dataRegistered) {
        return DataRegisteredResult.builder().data(Hex.encodeHexString(dataRegistered.getValue().toByteArray())).build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.DATA_REGISTERED;
    }
}
