package com.concordium.sdk.queries;

import com.concordium.sdk.responses.ProtocolVersion;
import com.concordium.sdk.responses.rewardstatus.RewardsOverview;
import com.concordium.sdk.transactions.CCDAmount;
import concordium.ConcordiumP2PRpc;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.util.Date;

public class GetRewardsOverviewTest {

    @SneakyThrows
    @Test
    public void shouldDeserializeJsonTest() {
        val json = "{" +
                "  \"bakingRewardAccount\": \"15\"," +
                "  \"finalizationRewardAccount\": \"4\"," +
                "  \"foundationTransactionRewards\": \"1827365\"," +
                "  \"gasAccount\": \"364547\"," +
                "  \"nextPaydayMintRate\": 2.61157877e-4," +
                "  \"nextPaydayTime\": \"2022-08-19T11:00:32.25Z\"," +
                "  \"protocolVersion\": 4," +
                "  \"totalAmount\": \"82510710429923844\"," +
                "  \"totalEncryptedAmount\": \"7954000000\"," +
                "  \"totalStakedCapital\": \"78276465710911895\"" +
                "}";
        val req = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();
        val res = RewardsOverview.fromJson(req);

        Assert.assertTrue(res.isPresent());
        Assert.assertEquals(res.get(), RewardsOverview.builder()
                .bakingRewardAccount(CCDAmount.fromMicro(15))
                .finalizationRewardAccount(CCDAmount.fromMicro(4))
                .foundationTransactionRewards(CCDAmount.fromMicro(1827365))
                .gasAccount(CCDAmount.fromMicro(364547))
                .nextPaydayMintRate(2.61157877e-4)
                .nextPaydayTime(Date.from(Instant.parse("2022-08-19T11:00:32.25Z")))
                .protocolVersion(ProtocolVersion.V4)
                .totalAmount(CCDAmount.fromMicro(82_510_710_429_923_844L))
                .totalEncryptedAmount(CCDAmount.fromMicro(7_954_000_000L))
                .totalStakedCapital(CCDAmount.fromMicro(78_276_465_710_911_895L))
                .build());
    }

    @Test
    public void shouldHandleNullJsonTest() {
        val json = "null";
        val req = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();
        val res = RewardsOverview.fromJson(req);

        Assert.assertFalse(res.isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldHandleInvalidJson() {
        val json = "{";
        val req = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();
        val res = RewardsOverview.fromJson(req);
    }
}
