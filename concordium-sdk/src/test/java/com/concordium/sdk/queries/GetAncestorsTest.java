package com.concordium.sdk.queries;

import com.concordium.sdk.responses.ancestors.Ancestors;
import com.concordium.sdk.transactions.Hash;
import concordium.ConcordiumP2PRpc;
import lombok.val;
import org.junit.Test;

import static org.junit.Assert.*;

public class GetAncestorsTest {

    @Test
    public void testShouldDeserializeJson() {
        val jsonRes = "[" +
                "\"9741d166fdc9b70a183d6c22f79e6f87c236f56c545c9b5f1114847fecc7ba39\"," +
                "\"b80b353df0bc3bb1c5c6e0771fe033c8507ad074bdb16d1a57580294fd36ec2c\"," +
                "\"21497d3c9412b322c61067e7a769579002b4f5983f893a03ea5feed936215872\"," +
                "\"2969c0d0fe7851fbcf1e857443260193555ca8173c98620e70ee56f8e53178e4\"," +
                "\"25450d32b2535878fd21d042316875a3bfc63ee84f0d3036e642c8eb3b360b3b\"" +
                "]";
        val jsonVal = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(jsonRes).build();
        val parsed = Ancestors.fromJson(jsonVal);

        assertTrue(parsed.isPresent());
        assertEquals(5, parsed.get().stream().count());
        assertArrayEquals(parsed.get().toArray(), new Hash[]{
                Hash.from("9741d166fdc9b70a183d6c22f79e6f87c236f56c545c9b5f1114847fecc7ba39"),
                Hash.from("b80b353df0bc3bb1c5c6e0771fe033c8507ad074bdb16d1a57580294fd36ec2c"),
                Hash.from("21497d3c9412b322c61067e7a769579002b4f5983f893a03ea5feed936215872"),
                Hash.from("2969c0d0fe7851fbcf1e857443260193555ca8173c98620e70ee56f8e53178e4"),
                Hash.from("25450d32b2535878fd21d042316875a3bfc63ee84f0d3036e642c8eb3b360b3b"),
        });
    }

    @Test
    public void testShouldHandleNullJson() {
        val jsonRes = "null";
        val jsonVal = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(jsonRes).build();
        val parsed = Ancestors.fromJson(jsonVal);

        assertFalse(parsed.isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShouldHandleInvalidJson() {
        val jsonRes = "[";
        val jsonVal = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(jsonRes).build();
        Ancestors.fromJson(jsonVal);
    }
}
