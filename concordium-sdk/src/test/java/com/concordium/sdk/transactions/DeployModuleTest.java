package com.concordium.sdk.transactions;

import com.concordium.sdk.transactions.smartcontracts.WasmModule;
import com.concordium.sdk.transactions.smartcontracts.WasmModuleVersion;
import com.concordium.sdk.types.Nonce;
import com.concordium.sdk.types.UInt64;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

import static com.concordium.sdk.transactions.TransactionTestHelper.getValidSigner;
import static org.junit.Assert.assertEquals;

public class DeployModuleTest {
    @SneakyThrows
    @Test
    public void testDeployVersionedModule() {
        val module = WasmModule.from(
                Files.readAllBytes(Paths.get("src/test/java/com/concordium/sdk/binaries/module.wasm.v1")));
        val payload = DeployModule.createNew(module, UInt64.from(6000))
                .withHeader(TransactionHeader
                        .builder()
                        .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                        .accountNonce(Nonce.from(78910))
                        .expiry(UInt64.from(123456))
                        .build())
                .signWith(getValidSigner());

        assertEquals(87146, payload.getBytes().length);
        assertEquals("5654319dff40a71f183104f8faf126be3dfc242918820381a9ac27838d89e72f",
                Hex.encodeHexString(payload.getDataToSign()));
        assertEquals("f1ca38ef26bd82515bb4be98b497fa2a33d3474fab27d001c54ac09219c5873f",
                payload.toAccountTransaction().getHash().asHex());
    }

    @SneakyThrows
    @Test
    public void testDeployUnVersionedModule() {
        val module = WasmModule.from(
                Files.readAllBytes(Paths.get("src/test/java/com/concordium/sdk/binaries/module.wasm")),
                WasmModuleVersion.V0);
        val payload = DeployModule.createNew(module, UInt64.from(6000))
                .withHeader(TransactionHeader
                        .builder()
                        .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                        .accountNonce(Nonce.from(78910))
                        .expiry(UInt64.from(123456))
                        .build())
                .signWith(getValidSigner());

        assertEquals(41541, payload.getBytes().length);
        assertEquals("f010b82e89fda27e785b3804e3a5cf474ee7601836f0daf355b67c5577d81c3a",
                Hex.encodeHexString(payload.getDataToSign()));
        assertEquals("5da6812f05b77f85f86b76f058a3328afba3a6888b66bbb6f27703a68bb201ec",
                payload.toAccountTransaction().getHash().asHex());
    }
}
