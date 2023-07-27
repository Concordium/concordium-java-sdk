package com.concordium.sdk.queries;

import com.concordium.sdk.responses.intanceinfo.InstanceInfo;
import com.concordium.sdk.responses.modulelist.ModuleRef;
import com.concordium.sdk.responses.transactionstatus.ContractVersion;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.transactions.CCDAmount;
import concordium.ConcordiumP2PRpc;
import lombok.val;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class GetInstanceInfoTest {

    @Test
    public void shouldDeserializeJson() {
        val json = "{" +
                "  \"owner\": \"4Y1c27ZRpRut9av69n3i1uhfeDp4XGuvsm9fkEjFvgpoxXWxQB\"," +
                "  \"amount\": \"0\"," +
                "  \"methods\": [\"weather.get\", \"weather.set\"]," +
                "  \"name\": \"init_weather\"," +
                "  \"sourceModule\": \"67d568433bd72e4326241f262213d77f446db8ba03dfba351ae35c1b2e7e5109\"," +
                "  \"version\": 1" +
                "}";
        val req = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();

        val instanceInfo = InstanceInfo.fromJson(req);

        Assert.assertTrue(instanceInfo.isPresent());
        Assert.assertEquals(instanceInfo.get(), new InstanceInfo(
                AccountAddress.from("4Y1c27ZRpRut9av69n3i1uhfeDp4XGuvsm9fkEjFvgpoxXWxQB"),
                CCDAmount.fromMicro(0),
                Arrays.asList("weather.get", "weather.set"),
                "init_weather",
                ModuleRef.from("67d568433bd72e4326241f262213d77f446db8ba03dfba351ae35c1b2e7e5109"),
                ContractVersion.V1
        ));
    }

    @Test
    public void shouldHandleNullJson() {
        val json = "null";
        val req = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();

        val instanceInfo = InstanceInfo.fromJson(req);

        Assert.assertFalse(instanceInfo.isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldHandleInvalidJson() {
        val json = "{";
        val req = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();

        InstanceInfo.fromJson(req);
    }
}
