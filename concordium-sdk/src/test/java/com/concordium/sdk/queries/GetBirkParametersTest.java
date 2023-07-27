package com.concordium.sdk.queries;

import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.responses.birkparamsters.Baker;
import com.concordium.sdk.responses.birkparamsters.BirkParameters;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.transactions.Hash;
import concordium.ConcordiumP2PRpc;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Assert;
import org.junit.Test;

public class GetBirkParametersTest {

    @SneakyThrows
    @Test
    public void shouldDeserializeJsonTest() {
        val json = "{" +
                "\"electionDifficulty\": 2.5e-2," +
                "\"electionNonce\": \"7d64801bc8cc5e74d4172bae4e73bbce1a38784179a78fd664850281af2f9964\"," +
                "\"bakers\": [" +
                "{" +
                "\"bakerId\": 0," +
                "\"bakerLotteryPower\": 9.090498249356058e-2," +
                "\"bakerAccount\": \"48XGRnvQoG92T1AwETvW5pnJ1aRSPMKsWtGdKhTqyiNZzMk3Qn\"" +
                "}," +
                "{" +
                "\"bakerId\": 1," +
                "\"bakerLotteryPower\": 9.09067382096406e-2," +
                "\"bakerAccount\": \"3U4sfVSqGG6XK8g6eho2qRYtnHc4MWJBG1dfxdtPGbfHwFxini\"" +
                "}" +
                "]" +
                "}";
        val req = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();
        val res = BirkParameters.fromJson(req);

        Assert.assertTrue(res.isPresent());
        Assert.assertEquals(res.get(), BirkParameters.builder()
                .electionDifficulty(2.5e-2)
                .electionNonce(Hash.from("7d64801bc8cc5e74d4172bae4e73bbce1a38784179a78fd664850281af2f9964"))
                .baker(Baker.builder()
                        .bakerId(AccountIndex.from(0))
                        .bakerLotteryPower(9.090498249356058e-2)
                        .bakerAccount(AccountAddress.from("48XGRnvQoG92T1AwETvW5pnJ1aRSPMKsWtGdKhTqyiNZzMk3Qn"))
                        .build())
                .baker(Baker.builder()
                        .bakerId(AccountIndex.from(1))
                        .bakerLotteryPower(9.09067382096406e-2)
                        .bakerAccount(AccountAddress.from("3U4sfVSqGG6XK8g6eho2qRYtnHc4MWJBG1dfxdtPGbfHwFxini"))
                        .build())
                .build());
    }

    @Test
    public void shouldHandleNullJsonTest() {
        val json = "null";
        val req = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();
        val res = BirkParameters.fromJson(req);

        Assert.assertFalse(res.isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldHandleInvalidJson() {
        val json = "{";
        val req = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();
        val res = BirkParameters.fromJson(req);
    }
}
