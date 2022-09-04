package com.concordium.sdk.queries;

import com.concordium.sdk.responses.modulelist.ModuleRef;
import com.concordium.sdk.transactions.Hash;
import concordium.ConcordiumP2PRpc;
import lombok.val;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

public class moduleListTest {

    @Test
    public void shouldDeserializeJsonTest() {
        val json = "[\"67d568433bd72e4326241f262213d77f446db8ba03dfba351ae35c1b2e7e5109\","
                + "\"67d568433bd72e4326241f262213d77f446db8ba03dfba351ae35c1b2e7e5109\","
                + "\"67d568433bd72e4326241f262213d77f446db8ba03dfba351ae35c1b2e7e5109\"]";

        val req = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();

        val ret = ModuleRef.fromJsonArray(req);
        val modref = Optional.ofNullable(ret);
        Assert.assertTrue(modref.isPresent());
        Assert.assertArrayEquals(modref.get().toArray(), new Hash[]{
                    Hash.from("67d568433bd72e4326241f262213d77f446db8ba03dfba351ae35c1b2e7e5109"),
                    Hash.from("67d568433bd72e4326241f262213d77f446db8ba03dfba351ae35c1b2e7e5109"),
                    Hash.from("67d568433bd72e4326241f262213d77f446db8ba03dfba351ae35c1b2e7e5109")
                }
        );
    }

    @Test
    public void shouldHandleNullJsonTest() {
        val json = "null";
        val req = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();

        val ret = ModuleRef.fromJsonArray(req);
        val modref = Optional.ofNullable(ret);

        Assert.assertFalse(modref.isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldHandleInvalidJsonTest() {
        val json = "{";
        val req = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();
        ModuleRef.fromJsonArray(req);
    }
}
