package com.concordium.sdk.queries;

import com.concordium.sdk.responses.blocksummary.updates.queues.AnonymityRevokerInfo;
import com.concordium.sdk.responses.blocksummary.updates.queues.Description;
import com.concordium.sdk.responses.rewardstatus.RewardsOverview;
import com.google.common.collect.ImmutableList;
import concordium.ConcordiumP2PRpc;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Assert;
import org.junit.Test;

public class GetAnonymityRevokersTest {

    @SneakyThrows
    @Test
    public void shouldDeserializeJsonTest() {
        val pubKey = "b14cbfe44a02c6b1f78711176d5f437295367aa4f2a8c2551ee10d25a03adc69d61a332a058971919dad7312e1fc94c58ed5281b5d117cb74068a5deef28f027c9055dd424b07043568ac040a4e51f3307f268a77eaebc36bd4bf7cdbbe238b8";
        val json = "[{" +
                "\"arIdentity\": 1," +
                "\"arDescription\": {" +
                "\"name\": \"Testnet AR 1\"," +
                "\"url\": \"\"," +
                "\"description\": \"Testnet anonymity revoker 1\"" +
                "}," +
                "\"arPublicKey\": \"" + pubKey + "\"}]";
        val req = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();
        val res = AnonymityRevokerInfo.fromJsonArray(req);

        Assert.assertTrue(res.isPresent());
        Assert.assertEquals(
                res.get(),
                ImmutableList.<AnonymityRevokerInfo>builder()
                        .add(new AnonymityRevokerInfo(1,
                                new Description("Testnet AR 1", "", "Testnet anonymity revoker 1"),
                                pubKey))
                        .build());
    }

    @Test
    public void shouldHandleNullJsonTest() {
        val json = "null";
        val req = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();
        val res = AnonymityRevokerInfo.fromJsonArray(req);

        Assert.assertFalse(res.isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldHandleInvalidJson() {
        val json = "{";
        val req = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();
        val res = AnonymityRevokerInfo.fromJsonArray(req);
    }
}
