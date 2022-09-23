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
        val res = BakerId.fromJsonArray(json);

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
        val res = BakerId.fromJsonArray(json);
        Assert.assertFalse(res.isPresent());
    }

    @Test
    public void shouldFailProperlyOnNull() {
        val res = BakerId.fromJsonArray(null);
        Assert.assertFalse(res.isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldHandleInvalidJson() {
        val json = "{";
        val res = BakerId.fromJsonArray(json);
    }
}
