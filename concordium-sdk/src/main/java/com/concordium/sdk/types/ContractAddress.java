package com.concordium.sdk.types;

import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.AccountType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableList;
import concordium.ConcordiumP2PRpc;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.Optional;

@Getter
@ToString
@EqualsAndHashCode
public final class ContractAddress extends AbstractAddress {

    @JsonProperty("subindex")
    private final long subIndex;

    @JsonProperty("index")
    private final long index;

    @JsonCreator
    public ContractAddress(@JsonProperty("subindex") long subIndex,
                           @JsonProperty("index") long index) {
        super(AccountType.ADDRESS_CONTRACT);
        this.subIndex = subIndex;
        this.index = index;
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
        return new ContractAddress(subIndex, index);
    }

    public byte[] getBytes() {
        val buffer = ByteBuffer.allocate(Long.BYTES + Long.BYTES);
        buffer.putLong(this.getIndex());
        buffer.putLong(this.getSubIndex());
        return buffer.array();
    }
}
