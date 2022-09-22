package com.concordium.sdk.queries;

import com.concordium.sdk.responses.modulelist.ModuleRef;
import com.google.common.collect.ImmutableList;
import concordium.ConcordiumP2PRpc;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Assert;
import org.junit.Test;

public class GetModulesListTest {

    @SneakyThrows
    @Test
    public void shouldDeserializeJsonTest() {
        val json = "[" +
                "\"00804f9a09361e0d372fe045d4deee7d57251fb5992305eeb030a88d65d85080\"," +
                "\"016fdecf92e687f829a47cb0eba9d595af4a96fe61e5d0a5eeb34e2b819583e4\"" +
                "]";
        val req = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();
        val res = ModuleRef.fromHashJsonArray(req);

        Assert.assertTrue(res.isPresent());
        Assert.assertEquals(res.get(), ImmutableList.<ModuleRef>builder()
                .add(ModuleRef.from("00804f9a09361e0d372fe045d4deee7d57251fb5992305eeb030a88d65d85080"))
                .add(ModuleRef.from("016fdecf92e687f829a47cb0eba9d595af4a96fe61e5d0a5eeb34e2b819583e4"))
                .build());
    }

    @Test
    public void shouldHandleNullJsonTest() {
        val json = "null";
        val req = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();
        val res = ModuleRef.fromHashJsonArray(req);

        Assert.assertFalse(res.isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldHandleInvalidJson() {
        val json = "{";
        val req = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();
        val res = ModuleRef.fromHashJsonArray(req);
    }
}
