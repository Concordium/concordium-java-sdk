package com.concordium.sdk.responses;

import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import concordium.ConcordiumP2PRpc;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

/**
 * Account Index of Baking Account.
 */
@Data
@ToString
@Getter
public class BakerId {

    private AccountIndex id;

    @JsonCreator
    BakerId(long index) {
        this.id = AccountIndex.from(index);
    }

    public static BakerId from(long id) {
        return new BakerId(id);
    }

    @Override
    public String toString() {
        return this.id.toString();
    }

    public static BakerId[] fromJsonArray(ConcordiumP2PRpc.JsonResponse res) {
        try {
            return JsonMapper.INSTANCE.readValue(res.getValue(), BakerId[].class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse Baker Ids Array JSON", e);
        }
    }
}
