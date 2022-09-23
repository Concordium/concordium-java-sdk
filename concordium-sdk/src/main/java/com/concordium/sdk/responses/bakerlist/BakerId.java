package com.concordium.sdk.responses.bakerlist;

import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableList;
import lombok.Data;
import lombok.val;

import java.util.Objects;
import java.util.Optional;

/**
 * Account index of a baker.
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
}
