package com.concordium.sdk.queries;

import com.concordium.sdk.responses.bakerlist.BakerId;
import com.google.common.collect.ImmutableList;
import concordium.ConcordiumP2PRpc;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Assert;
import org.junit.Test;

public class GetBakerListTest {

    @SneakyThrows
    @Test
    public void shouldDeserializeJsonTest() {
        val json = "[0,1,2,3]";
        val req = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();
        val res = BakerId.fromJsonArray(req);

        Assert.assertTrue(res.isPresent());
        Assert.assertEquals(res.get(), ImmutableList.builder()
                .add(BakerId.from(0))
                .add(BakerId.from(1))
                .add(BakerId.from(2))
                .add(BakerId.from(3))
                .build());
    }

    @Test
    public void shouldHandleNullJsonTest() {
        val json = "null";
        val req = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();
        val res = BakerId.fromJsonArray(req);

        Assert.assertFalse(res.isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldHandleInvalidJson() {
        val json = "{";
        val req = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();
        val res = BakerId.fromJsonArray(req);
    }
}
