package com.concordium.sdk.queries;

import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.responses.accountinfo.CommissionRates;
import com.concordium.sdk.responses.poolstatus.*;
import com.concordium.sdk.responses.transactionstatus.OpenStatus;
import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.UInt64;
import concordium.ConcordiumP2PRpc;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

public class GetPoolStatusTest {

    @SneakyThrows
    @Test
    public void shouldDeserializeBakerPoolJsonTest() {
        val json = "{" +
                "\"poolType\": \"BakerPool\"," +
                "\"bakerId\": 1," +
                "\"bakerAddress\": \"3U4sfVSqGG6XK8g6eho2qRYtnHc4MWJBG1dfxdtPGbfHwFxini\"," +
                "\"bakerEquityCapital\": \"7115794301385111\"," +
                "\"delegatedCapital\": \"0\"," +
                "\"delegatedCapitalCap\": \"790946966340087\"," +
                "\"poolInfo\": {" +
                "\"openStatus\": \"closedForAll\"," +
                "\"metadataUrl\": \"\"," +
                "\"commissionRates\": {" +
                "\"bakingCommission\": 0.1," +
                "\"finalizationCommission\": 1.0," +
                "\"transactionCommission\": 0.1" +
                "}" +
                "}," +
                "\"bakerStakePendingChange\": { \"pendingChangeType\": \"NoChange\" }," +
                "\"currentPaydayStatus\": {" +
                "\"blocksBaked\": 12," +
                "\"finalizationLive\": true," +
                "\"transactionFeesEarned\": \"2522839\"," +
                "\"effectiveStake\": \"7114034220201662\"," +
                "\"lotteryPower\": 9.09067382096406e-2," +
                "\"bakerEquityCapital\": \"7114034220201662\"," +
                "\"delegatedCapital\": \"0\"" +
                "}," +
                "\"allPoolTotalCapital\": \"78276465710911895\"" +
                "}";
        val req = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();
        val res = PoolStatus.fromJson(req);

        Assert.assertTrue(res.isPresent());
        Assert.assertEquals(res.get(), BakerPoolStatus.builder()
                .bakerId(BakerId.from(1))
                .bakerAddress(AccountAddress.from("3U4sfVSqGG6XK8g6eho2qRYtnHc4MWJBG1dfxdtPGbfHwFxini"))
                .bakerEquityCapital(CCDAmount.fromMicro(7_115_794_301_385_111L))
                .delegatedCapital(CCDAmount.fromMicro(0))
                .delegatedCapitalCap(CCDAmount.fromMicro(790_946_966_340_087L))
                .poolInfo(PoolInfo.builder()
                        .openStatus(OpenStatus.CLOSED_FOR_ALL)
                        .metadataUrl("")
                        .commissionRates(CommissionRates.builder()
                                .bakingCommission(0.1)
                                .finalizationCommission(1.0)
                                .transactionCommission(0.1)
                                .build())
                        .build())
                .bakerStakePendingChange(new PendingChangeNoChange())
                .currentPaydayStatus(Optional.of(CurrentPaydayStatus.builder()
                        .blocksBaked(UInt64.from(12))
                        .finalizationLive(true)
                        .transactionFeesEarned(CCDAmount.fromMicro(2_522_839L))
                        .effectiveStake(CCDAmount.fromMicro(7_114_034_220_201_662L))
                        .lotteryPower(9.09067382096406e-2)
                        .bakerEquityCapital(CCDAmount.fromMicro(7_114_034_220_201_662L))
                        .delegatedCapital(CCDAmount.fromMicro(0))
                        .build()))
                .allPoolTotalCapital(CCDAmount.fromMicro(78_276_465_710_911_895L))
                .build());
    }

    @SneakyThrows
    @Test
    public void shouldDeserializePassiveDelegationJsonTest() {
        val json = "{" +
                "\"poolType\": \"PassiveDelegation\"," +
                "\"delegatedCapital\": \"675324401271\"," +
                "\"commissionRates\": {" +
                "\"bakingCommission\": 0.12," +
                "\"finalizationCommission\": 1.0," +
                "\"transactionCommission\": 0.12" +
                "}," +
                "\"currentPaydayTransactionFeesEarned\": \"129\"," +
                "\"currentPaydayDelegatedCapital\": \"675226245293\"," +
                "\"allPoolTotalCapital\": \"78276465710911895\"" +
                "}";
        val req = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();
        val res = PoolStatus.fromJson(req);

        Assert.assertTrue(res.isPresent());
        Assert.assertEquals(res.get(), PassiveDelegationStatus.builder()
                .delegatedCapital(CCDAmount.fromMicro(675_324_401_271L))
                .commissionRates(CommissionRates.builder()
                        .bakingCommission(0.12)
                        .finalizationCommission(1.0)
                        .transactionCommission(0.12)
                        .build())
                .currentPaydayTransactionFeesEarned(CCDAmount.fromMicro(129L))
                .currentPaydayDelegatedCapital(CCDAmount.fromMicro(675_226_245_293L))
                .allPoolTotalCapital(CCDAmount.fromMicro(78_276_465_710_911_895L))
                .build());
    }

    @Test
    public void shouldHandleNullJsonTest() {
        val json = "null";
        val req = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();
        val res = PoolStatus.fromJson(req);

        Assert.assertFalse(res.isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldHandleInvalidJson() {
        val json = "{";
        val req = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();
        val res = PoolStatus.fromJson(req);
    }
}
