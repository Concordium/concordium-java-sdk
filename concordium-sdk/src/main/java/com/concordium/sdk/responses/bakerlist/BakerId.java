package com.concordium.sdk.responses.bakerlist;

import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import concordium.ConcordiumP2PRpc;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

/**
 * Short id of the baker.
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
