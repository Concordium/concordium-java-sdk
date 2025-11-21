package com.concordium.sdk.crypto.wallet.web3Id;

import com.concordium.sdk.serializing.CborMapper;
import com.concordium.sdk.transactions.Hash;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.codec.binary.Hex;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class VerificationRequestAnchorTest {

    @Test
    @SneakyThrows
    public void cborSerialization() {
        val anchor = VerificationRequestAnchor
                .builder()
                .version(VerificationRequestAnchor.V1)
                .hash(Hash.from("3d52e63350bfd21676ecbf6ce29688e3be6bff86cbacfe138aac107b64d29ba1"))
                .build();
        val expectedHex = "bf6776657273696f6e01646861736858203d52e63350bfd21676ecbf6ce29688e3be6bff86cbacfe138aac107b64d29ba1647479706566434344565241ff";
        Assert.assertEquals(
                expectedHex,
                Hex.encodeHexString(CborMapper.INSTANCE.writeValueAsBytes(anchor))
        );
        Assert.assertEquals(
                anchor,
                CborMapper.INSTANCE.readValue(Hex.decodeHex(expectedHex), VerificationRequestAnchor.class)
        );
    }

    @Test
    @SneakyThrows
    public void cborSerializationWithSimplePublicInfo() {
        val publicInfo = new ArrayList<Integer>();
        publicInfo.add(1);
        publicInfo.add(2);
        publicInfo.add(3);
        val anchor = VerificationRequestAnchor
                .builder()
                .version(VerificationRequestAnchor.V1)
                .hash(Hash.from("3d52e63350bfd21676ecbf6ce29688e3be6bff86cbacfe138aac107b64d29ba1"))
                .publicInfo(publicInfo)
                .build();
        val expectedHex = "bf6776657273696f6e01646861736858203d52e63350bfd21676ecbf6ce29688e3be6bff86cbacfe138aac107b64d29ba1647479706566434344565241667075626c696383010203ff";
        Assert.assertEquals(
                expectedHex,
                Hex.encodeHexString(CborMapper.INSTANCE.writeValueAsBytes(anchor))
        );
        Assert.assertEquals(
                anchor,
                CborMapper.INSTANCE.readValue(Hex.decodeHex(expectedHex), VerificationRequestAnchor.class)
        );
    }

    @Test
    @SneakyThrows
    public void cborDeserializationWithComplexPublicInfo() {
        val publicInfo = new LinkedHashMap<String, String>();
        publicInfo.put("purpose", "Age verification");
        publicInfo.put("verifier", "Test Verifier");
        val anchor = VerificationRequestAnchor
                .builder()
                .version(VerificationRequestAnchor.V1)
                .hash(Hash.from("eb2499bd1ab8f24357945bb59e05a51c4cf19631d20bf1cb1bfc2c5974083ca9"))
                .publicInfo(publicInfo)
                .build();
        val expectedHex = "a464686173685820eb2499bd1ab8f24357945bb59e05a51c4cf19631d20bf1cb1bfc2c5974083ca9647479706566434344565241667075626c6963a267707572706f73657041676520766572696669636174696f6e6876657269666965726d546573742056657269666965726776657273696f6e01";
        Assert.assertEquals(
                anchor,
                CborMapper.INSTANCE.readValue(Hex.decodeHex(expectedHex), VerificationRequestAnchor.class)
        );
    }
}
