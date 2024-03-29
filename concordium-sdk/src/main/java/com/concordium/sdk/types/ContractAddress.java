package com.concordium.sdk.types;

import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.AccountType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.io.IOException;
import java.nio.ByteBuffer;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@JsonSerialize(using = ContractAddress.ContractAddressSerializer.class)
public final class ContractAddress extends AbstractAddress {

    @JsonProperty("index")
    private final long index;

    @JsonProperty("subindex")
    private final long subIndex;

    @JsonCreator
    @Builder
    public ContractAddress(@JsonProperty("subindex") long subIndex,
                           @JsonProperty("index") long index) {
        super(AccountType.ADDRESS_CONTRACT);
        this.subIndex = subIndex;
        this.index = index;
    }

    public static ContractAddress from(com.concordium.grpc.v2.ContractAddress address) {
        return ContractAddress.from(address.getIndex(), address.getSubindex());
    }

    public static ContractAddress from(ByteBuffer source) {
        long contractIndex = source.getLong();
        long contractSubIndex = source.getLong();
        return new ContractAddress(contractSubIndex, contractIndex);
    }

    public String toJson() {
        try {
            return JsonMapper.INSTANCE.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Could not serialize Contract Address");
        }
    }

    public static ContractAddress from(long index, long subIndex) {
        return ContractAddress.builder()
                .index(index)
                .subIndex(subIndex)
                .build();
    }

    public byte[] getBytes() {
        val buffer = ByteBuffer.allocate(Long.BYTES + Long.BYTES);
        buffer.putLong(this.getIndex());
        buffer.putLong(this.getSubIndex());
        return buffer.array();
    }

    static class ContractAddressSerializer extends JsonSerializer<ContractAddress> {
        @Override
        public void serialize(ContractAddress o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

            jsonGenerator.writeStartObject();

            jsonGenerator.writeNumberField("index", o.getIndex());
            jsonGenerator.writeNumberField("subindex", o.getSubIndex());

            jsonGenerator.writeEndObject();
        }
    }
}
