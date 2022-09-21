package com.concordium.sdk.queries;

import com.concordium.sdk.transactions.AccountAddress;
import concordium.ConcordiumP2PRpc;
import lombok.val;
import org.junit.Assert;
import org.junit.Test;

public class GetAccountListTest {

    @Test
    public void shouldDeserializeJsonTest() {
        val json = "[\"2woqsM8vniCpyd92LKESejT8wY3kvAZivgumra7Er424ThVrRF\","
                + "\"2wrnmNgH5jHdLRjudusWMhNS9iJbPuyTR1u4Ujdf6VvgjuTGZL\","
                + "\"2wwpK1VArs7PbRRWMxsqUcz4UpjRocqVUwmfCnxwmZF6xSJ5ZT\"]";
        val req = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();
        val res = AccountAddress.toList(req);

        Assert.assertTrue(res.isPresent());
        Assert.assertArrayEquals(res.get().toArray(), new AccountAddress[]{
                AccountAddress.from("2woqsM8vniCpyd92LKESejT8wY3kvAZivgumra7Er424ThVrRF"),
                AccountAddress.from("2wrnmNgH5jHdLRjudusWMhNS9iJbPuyTR1u4Ujdf6VvgjuTGZL"),
                AccountAddress.from("2wwpK1VArs7PbRRWMxsqUcz4UpjRocqVUwmfCnxwmZF6xSJ5ZT"),
        });
    }

    @Test
    public void shouldHandleNullJsonTest() {
        val json = "null";
        val req = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();
        val res = AccountAddress.toList(req);

        Assert.assertFalse(res.isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldHandleInvalidJsonTest() {
        val json = "{";
        val req = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();
        AccountAddress.toList(req);
    }
}
