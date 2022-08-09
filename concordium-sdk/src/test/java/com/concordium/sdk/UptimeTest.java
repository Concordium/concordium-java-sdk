package com.concordium.sdk;

import com.concordium.sdk.Client;
import com.concordium.sdk.Connection;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UptimeTest {

    @SneakyThrows
    @Test
    public void uptimeTest() {
        Connection connection = Connection.builder()
                .credentials(Credentials.from("rpcadmin"))
                .host("localhost")
                .port(10001)
                .timeout(15000)
                .build();
        Client client = Client.from(connection);
        val uptime = client.uptime();
        assertEquals(uptime.getSeconds(), 10);
    }
}
