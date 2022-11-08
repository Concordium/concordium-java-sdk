package com.concordium.sdk.queries;

import com.concordium.sdk.types.ContractAddress;
import concordium.ConcordiumP2PRpc;
import lombok.val;
import org.junit.Assert;
import org.junit.Test;

public class GetInstancesTest {

    @Test
    public void shouldDeserializeJsonTest() {
        val json = "[{\"index\":0,\"subindex\":0},{\"index\":1,\"subindex\":0},{\"index\":2,\"subindex\":0}]";
        val req = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();

        val address = ContractAddress.toList(req);

        Assert.assertTrue(address.isPresent());
        Assert.assertArrayEquals(address.get().toArray(), new ContractAddress[]{
                new ContractAddress(0, 0),
                new ContractAddress(0, 1),
                new ContractAddress(0, 2),
        });
    }

    @Test
    public void shouldHandleNullJsonTest() {
        val json = "null";
        val req = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();

        val address = ContractAddress.toList(req);

        Assert.assertFalse(address.isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldHandleInvalidJsonTest() {
        val json = "{";
        val req = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();
        ContractAddress.toList(req);
    }
}
