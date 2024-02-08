package com.concordium.sdk.transactions;

import lombok.val;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;

public class ReceiveNameTest {

    @Test
    public void testSerializeAndDeserializeReceiveName() {
        val receiveName = ReceiveName.from("mycontract", "myfunction");
        assertEquals(receiveName, ReceiveName.from(ByteBuffer.wrap(receiveName.getBytes())));
    }
}
