package com.concordium.sdk.responses.bakerlist;

import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableList;
import concordium.ConcordiumP2PRpc;
import lombok.Data;
import lombok.val;

import java.util.Optional;

/**
 * Account Index of Baking Account.
 */
@Data
public class BakerId {

    private AccountIndex id;

    @JsonCreator
    BakerId(long index) {
        this.id = AccountIndex.from(index);
    }

    @Override
    public String toString() {
        return this.id.toString();
    }

    public static Optional<ImmutableList<BakerId>> fromJsonArray(ConcordiumP2PRpc.JsonResponse res) {
        try {
            val array = JsonMapper.INSTANCE.readValue(res.getValue(), BakerId[].class);

            return Optional.ofNullable(array).map(arr -> ImmutableList.copyOf(arr));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse Baker Ids Array JSON", e);
        }
    }
}
