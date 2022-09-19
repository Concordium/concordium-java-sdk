package com.concordium.sdk.queries;

import com.concordium.sdk.responses.branch.Branch;
import concordium.ConcordiumP2PRpc;
import lombok.val;
import org.junit.Test;

import static org.junit.Assert.*;

public class GetBranchesTest {

    @Test
    public void testShouldDeserializeJson() {
        val BLOCK_HASH_1 = "730d1f12e384915e8c0e0f2423afcb927e7340f8ce956fa4363ea4f69656c087";
        val BLOCK_HASH_2 = "6988bb6042e80e3d661b111142096b966bfebfa486a06aaacd428b0cc8a925f5";

        val branch = Branch.fromJson(ConcordiumP2PRpc.JsonResponse.newBuilder()
                .setValue("{\n" +
                        "  \"blockHash\": \"" + BLOCK_HASH_1 + "\",\n" +
                        "  \"children\": [\n" +
                        "    {\n" +
                        "      \"blockHash\": \"" + BLOCK_HASH_2 + "\",\n" +
                        "      \"children\": []\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}\n")
                .build());

        assertEquals(BLOCK_HASH_1, branch.getBlockHash().asHex());
        assertEquals(branch.getChildren().stream().count(), 1);
        assertEquals(branch.getChildren().stream().findFirst().get().getBlockHash().asHex(), BLOCK_HASH_2);
        assertEquals(branch.getChildren().stream().findFirst().get().getChildren().stream().count(), 0);
    }

    @Test
    public void testShouldDeserializeNullJson() {
        val branches = Branch.fromJson(ConcordiumP2PRpc.JsonResponse.newBuilder().setValue("null").build());
        assertNull(branches);
    }

    @Test
    public void testShouldHandleInvalidJson() {
        try {
            Branch.fromJson(ConcordiumP2PRpc.JsonResponse.newBuilder().setValue("{").build());
        } catch (Exception e) {
            assertSame(IllegalArgumentException.class, e.getClass());
        }
    }
}
