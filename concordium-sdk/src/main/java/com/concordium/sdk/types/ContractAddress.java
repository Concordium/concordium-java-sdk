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
import com.google.common.collect.ImmutableList;
import concordium.ConcordiumP2PRpc;
import lombok.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.Optional;

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

    public String toJson() {
        try {
            return JsonMapper.INSTANCE.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Could not serialize Contract Address");
        }
    }

    public static Optional<ImmutableList<ContractAddress>> toList(ConcordiumP2PRpc.JsonResponse res) {
        try {
            val array = JsonMapper.INSTANCE.readValue(res.getValue(), ContractAddress[].class);

            if (Objects.isNull(array)) {
                return Optional.empty();
            }

            return Optional.of(ImmutableList.copyOf(array));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse ContractAddress Array JSON", e);
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
