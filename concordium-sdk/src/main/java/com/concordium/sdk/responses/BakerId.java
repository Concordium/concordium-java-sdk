package com.concordium.sdk.responses;

import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableList;
import lombok.Data;
import lombok.val;

import java.util.Objects;
import java.util.Optional;

/**
 * Account index of the baking account.
 */
@Data
public class BakerId {

    /**
     * The account index of the baker.
     */
    private final AccountIndex id;

    private BakerId(long index) {
        this.id = AccountIndex.from(index);
    }

    private BakerId(AccountIndex accountIndex) {
        this.id = accountIndex;
    }

    @Override
    public String toString() {
        return this.id.toString();
    }

    public long toLong() {
        return this.id.getIndex().getValue();
    }

    public static BakerId from(AccountIndex accountIndex) {
        return new BakerId(accountIndex);
    }

    @JsonCreator
    public static BakerId from(long index) {
        return new BakerId(index);
    }

    public static Optional<ImmutableList<BakerId>> fromJsonArray(String jsonValue) {
        try {
            if (Objects.isNull(jsonValue)) {
                return Optional.empty();
            }
            val array = JsonMapper.INSTANCE.readValue(jsonValue, BakerId[].class);

            return Optional.ofNullable(array).map(arr -> ImmutableList.copyOf(arr));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse Baker Ids Array JSON", e);
        }
    }

    public byte[] getBytes() {
        return this.id.getBytes();
    }
}
