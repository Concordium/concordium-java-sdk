package com.concordium.sdk.transactions;

import com.concordium.sdk.transactions.smartcontracts.WasmModule;
import com.concordium.sdk.transactions.smartcontracts.WasmModuleVersion;
import com.concordium.sdk.types.AccountAddress;
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
        val payload = DeployModule.createNew(module)
                .withHeader(TransactionHeader
                        .explicitMaxEnergyBuilder()
                        .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                        .accountNonce(Nonce.from(78910))
                        .expiry(UInt64.from(123456))
                        .maxEnergyCost(UInt64.from(6000))
                        .build())
                .signWith(getValidSigner());

        assertEquals(87146, payload.getBytes().length);
        assertEquals("3357c9fe9a91ceb3b4189792a2a79b2c3ce495f3d16bdf745967b2761bfa033d",
                Hex.encodeHexString(payload.getDataToSign()));
        assertEquals("d6ec6b4df96e318df0cffcbc20cb8613b79ff564b3d130f2cd7502550478c78a",
                payload.toAccountTransaction().getHash().asHex());
    }

    @SneakyThrows
    @Test
    public void testDeployUnVersionedModule() {
        val module = WasmModule.from(
                Files.readAllBytes(Paths.get("src/test/java/com/concordium/sdk/binaries/module.wasm")),
                WasmModuleVersion.V0);
        val payload = DeployModule.createNew(module)
                .withHeader(TransactionHeader
                        .explicitMaxEnergyBuilder()
                        .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                        .accountNonce(Nonce.from(78910))
                        .expiry(UInt64.from(123456))
                        .maxEnergyCost(UInt64.from(6000))
                        .build())
                .signWith(getValidSigner());

        assertEquals(41541, payload.getBytes().length);
        assertEquals("8511703a46003dd6214b51bdb80507aec26d942236bf2b01a91911d044cfe96e",
                Hex.encodeHexString(payload.getDataToSign()));
        assertEquals("5959be8bbb0214632d4303a9164722a928ee188105e087f2912c4e2ac5fbef8b",
                payload.toAccountTransaction().getHash().asHex());
    }
}
