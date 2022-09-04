package com.concordium.sdk.queries;

import com.concordium.sdk.responses.birkparameters.Baker;
import com.concordium.sdk.responses.birkparameters.BirkParameters;
import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.Hash;
import com.concordium.sdk.types.UInt64;
import concordium.ConcordiumP2PRpc;
import lombok.val;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Optional;

public class brickParametersTest {

    @Test
    public void shouldDeserializeJsonTest() {
        val json = "{\"electionDifficulty\":\"0\",\"electionNonce\":\"67d568433bd72e4326241f262213d77f446db8ba03dfba351ae35c1b2e7e5109\",\"bakers\":[{\"bakerId\":10,\"bakerLotteryPower\":0,\"bakerAccount\":\"4Y1c27ZRpRut9av69n3i1uhfeDp4XGuvsm9fkEjFvgpoxXWxQB\"},{\"bakerId\":10,\"bakerLotteryPower\":0,\"bakerAccount\":\"4Y1c27ZRpRut9av69n3i1uhfeDp4XGuvsm9fkEjFvgpoxXWxQB\"}]}";

        val req = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();

        val ret = BirkParameters.fromJson(req);
        val reward = Optional.ofNullable(ret);
        Assert.assertTrue(reward.isPresent());
        Assert.assertEquals(reward.get(), new BirkParameters(
                Double.valueOf(0),
                Hash.from("67d568433bd72e4326241f262213d77f446db8ba03dfba351ae35c1b2e7e5109"),
                Arrays.asList(
                    new Baker (
                        UInt64.from(10),
                        Double.valueOf(0),
                        AccountAddress.from("4Y1c27ZRpRut9av69n3i1uhfeDp4XGuvsm9fkEjFvgpoxXWxQB")
                    ),
                    new Baker (
                        UInt64.from(10),
                        Double.valueOf(0),
                        AccountAddress.from("4Y1c27ZRpRut9av69n3i1uhfeDp4XGuvsm9fkEjFvgpoxXWxQB")
                    )
                )
            )
        );
    }

    @Test
    public void shouldHandleNullJsonTest() {
        val json = "null";
        val req = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();

        val ret = BirkParameters.fromJson(req);
        val reward = Optional.ofNullable(ret);

        Assert.assertFalse(reward.isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldHandleInvalidJsonTest() {
        val json = "{";
        val req = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();
        BirkParameters.fromJson(req);
    }
}
