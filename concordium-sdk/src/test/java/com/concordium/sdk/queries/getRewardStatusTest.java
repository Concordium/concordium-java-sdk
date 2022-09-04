package com.concordium.sdk.queries;

import com.concordium.sdk.responses.ProtocolVersion;
import com.concordium.sdk.responses.rewardstatus.RewardsOverview;
import com.concordium.sdk.transactions.CCDAmount;
import concordium.ConcordiumP2PRpc;
import lombok.val;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.Optional;

public class getRewardStatusTest {

    @Test
    public void shouldDeserializeJsonTest() {
        val json = "{\"bakingRewardAccount\":\"0\",\"finalizationRewardAccount\":\"0\",\"foundationTransactionRewards\":\"0\",\"gasAccount\":\"0\",\"nextPaydayMintRate\":\"0\",\"nextPaydayTime\":1000,\"protocolVersion\":1,\"totalAmount\":\"0\",\"totalEncryptedAmount\":\"0\",\"totalStakedCapital\":\"0\"}";

        val req = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();

        val ret = RewardsOverview.fromJson(req);
        val reward = Optional.ofNullable(ret);

        Assert.assertTrue(reward.isPresent());
        Assert.assertEquals(reward.get(), new RewardsOverview(
                CCDAmount.fromMicro(0),
                CCDAmount.fromMicro(0),
                CCDAmount.fromMicro(0),
                CCDAmount.fromMicro(0),
                Double.valueOf(0),
                new Date(1000),
                ProtocolVersion.forValue(1),
                CCDAmount.fromMicro(0),
                CCDAmount.fromMicro(0),
                CCDAmount.fromMicro(0)
        ));
    }

    @Test
    public void shouldHandleNullJsonTest() {
        val json = "null";
        val req = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();

        val ret = RewardsOverview.fromJson(req);
        val reward = Optional.ofNullable(ret);

        Assert.assertFalse(reward.isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldHandleInvalidJsonTest() {
        val json = "{";
        val req = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();
        RewardsOverview.fromJson(req);
    }
}
