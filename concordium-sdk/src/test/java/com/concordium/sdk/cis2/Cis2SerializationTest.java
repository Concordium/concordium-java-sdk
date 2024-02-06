package com.concordium.sdk.cis2;

import com.concordium.sdk.cis2.events.BurnEvent;
import com.concordium.sdk.cis2.events.Cis2Event;
import com.concordium.sdk.cis2.events.MintEvent;
import com.concordium.sdk.cis2.events.TransferEvent;
import com.concordium.sdk.transactions.Parameter;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.UInt16;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.util.Arrays;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class Cis2SerializationTest {

    @SneakyThrows
    @Test
    public void testDeserializeTransferEvent(){
        val event = Hex.decodeHex("ff00d0ff9f05009e15fc57bbe167411d4d9c0686e31e8e937d751625972f7c566de4a97f650dc500fd3dd07c83e42461554cf0dd90d73c1ff04531fc2b9c90b9762df8793319e48d");
        val cis2Event = SerializationUtils.deserializeCis2Event(event);
        assertEquals(Cis2Event.Type.TRANSFER, cis2Event.getType());
        val transferEvent = (TransferEvent) cis2Event;
        assertEquals("", transferEvent.getHexTokenId());
        assertEquals(11010000, transferEvent.getTokenAmount());
        assertEquals(AccountAddress.from("49NGYqmPtbuCkXSQt7298mL6Xp52UpSR4U2jVzJjKW9P3b3whw"), transferEvent.getFrom());
        assertEquals(AccountAddress.from("4sGtbuGKgakv5pKSMsy3CEQbW3sn2PbTzTVLZLA6zxX5bB3C5a"), transferEvent.getTo());
    }

    @SneakyThrows
    @Test
    public void testDeserializeMintEvent(){
        val event = Hex.decodeHex("fe00c0843d0097567a23128fb54e3f9fa2f0236f7468ed3c61c4f66a92907d4162f5742ec3ca");
        val cis2Event = SerializationUtils.deserializeCis2Event(event);
        assertEquals(Cis2Event.Type.MINT, cis2Event.getType());
        val mintEvent = (MintEvent) cis2Event;
        assertEquals("", mintEvent.getHexTokenId());
        assertEquals(1000000, mintEvent.getTokenAmount());
        assertEquals(AccountAddress.from("46Pu3wVfURgihzAXoDxMxWucyFo5irXvaEmacNgeK7i49MKyiD"), mintEvent.getOwner());
    }

    @SneakyThrows
    @Test
    public void testDeserializeBurnEvent(){
        val event = Hex.decodeHex("fd00c0843d0097567a23128fb54e3f9fa2f0236f7468ed3c61c4f66a92907d4162f5742ec3ca");
        val cis2Event = SerializationUtils.deserializeCis2Event(event);
        assertEquals(Cis2Event.Type.BURN, cis2Event.getType());
        val burnEvent = (BurnEvent) cis2Event;
        assertEquals("", burnEvent.getHexTokenId());
        assertEquals(1000000, burnEvent.getTokenAmount());
        assertEquals(AccountAddress.from("46Pu3wVfURgihzAXoDxMxWucyFo5irXvaEmacNgeK7i49MKyiD"), burnEvent.getOwner());
    }

    @SneakyThrows
    @Test
    public void testSerializeTransfer(){
        // this is a raw transfer parameter i.e. the length of the actual parameter is not included.
        val expectedParameter = Hex.decodeHex("010000a995a405009e15fc57bbe167411d4d9c0686e31e8e937d751625972f7c566de4a97f650dc500fd3dd07c83e42461554cf0dd90d73c1ff04531fc2b9c90b9762df8793319e48d0000");
        val transfers = new ArrayList<Cis2Transfer>();
        transfers.add(new Cis2Transfer(new byte[0], 11078313, AccountAddress.from("49NGYqmPtbuCkXSQt7298mL6Xp52UpSR4U2jVzJjKW9P3b3whw"), AccountAddress.from("4sGtbuGKgakv5pKSMsy3CEQbW3sn2PbTzTVLZLA6zxX5bB3C5a"), null));
        Parameter parameter = SerializationUtils.serializeTransfers(transfers);
        byte[] lengthBytes = UInt16.from(expectedParameter.length).getBytes();
        assertArrayEquals(Arrays.concatenate(lengthBytes, expectedParameter), parameter.getBytes());
    }

    @SneakyThrows
    @Test
    public void testSerializeTransferWithAdditionalData(){
        // this is a raw transfer parameter i.e. the length of the actual parameter is not included.
        val expectedParameter = Hex.decodeHex("010000a995a405009e15fc57bbe167411d4d9c0686e31e8e937d751625972f7c566de4a97f650dc500fd3dd07c83e42461554cf0dd90d73c1ff04531fc2b9c90b9762df8793319e48d010001");
        val transfers = new ArrayList<Cis2Transfer>();
        transfers.add(new Cis2Transfer(new byte[0], 11078313, AccountAddress.from("49NGYqmPtbuCkXSQt7298mL6Xp52UpSR4U2jVzJjKW9P3b3whw"), AccountAddress.from("4sGtbuGKgakv5pKSMsy3CEQbW3sn2PbTzTVLZLA6zxX5bB3C5a"), new byte[]{1}));
        Parameter parameter = SerializationUtils.serializeTransfers(transfers);
        byte[] lengthBytes = UInt16.from(expectedParameter.length).getBytes();
        assertArrayEquals(Arrays.concatenate(lengthBytes, expectedParameter), parameter.getBytes());
    }
}
