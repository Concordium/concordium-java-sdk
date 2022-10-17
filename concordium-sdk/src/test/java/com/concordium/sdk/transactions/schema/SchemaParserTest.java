package com.concordium.sdk.transactions.schema;

import lombok.SneakyThrows;
import lombok.val;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import static java.nio.file.Files.readAllBytes;
import static org.junit.Assert.*;

public class SchemaParserTest {
    private static byte[] CIS2_V2_SCHEMA_BYTES;
    private static byte[] SCHEMA_BYTES;

    static {
        try {
            CIS2_V2_SCHEMA_BYTES = readAllBytes(Paths.get("src/test/java/com/concordium/sdk/binaries/schema.bin.v2"));
            SCHEMA_BYTES = readAllBytes(Paths.get("src/test/java/com/concordium/sdk/binaries/notary_smart_contract_schema.bin"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    @Test
    public void shouldParseV2Cis2MintSchema() {
        final ModuleSchema schema = new SchemaParser().parse(CIS2_V2_SCHEMA_BYTES);
        assertEquals(SchemaVersion.V2, schema.getVersion());

        val contract = schema.getContract("CIS2-NFT");
        assertNotNull(contract);

        val mintFn = contract.getReceiveFunction("mint");
        assertNotNull(mintFn);

        val mintFuncParams = mintFn.getParameter().get();
        val expectedMintFuncParams = SchemaTypeStruct.builder()
                .fields(SchemaTypeFieldsNamed.builder()
                        .field(SchemaTypeFieldNamed.builder()
                                .name("owner")
                                .type(SchemaTypeEnum.builder()
                                        .variant(SchemaTypeEnumVariant.builder()
                                                .name("Account")
                                                .fields(SchemaTypeFieldsUnNamed.builder()
                                                        .field(new SchemaTypeAccountAddress())
                                                        .build())
                                                .build())
                                        .variant(SchemaTypeEnumVariant.builder()
                                                .name("Contract")
                                                .fields(SchemaTypeFieldsUnNamed.builder()
                                                        .field(new SchemaTypeContractAddress())
                                                        .build())
                                                .build())
                                        .build())
                                .build())
                        .field(SchemaTypeFieldNamed.builder()
                                .name("tokens")
                                .type(new SchemaTypeMap(
                                        SizeLength.from(0),
                                        SchemaTypeTuple.builder()
                                                .left(SchemaTypeByteList.builder()
                                                        .sizeLength(SizeLength.from(0))
                                                        .build())
                                                .right(SchemaTypeStruct.builder()
                                                        .fields(SchemaTypeFieldsNamed.builder()
                                                                .field(SchemaTypeFieldNamed.builder()
                                                                        .name("url")
                                                                        .type(SchemaTypeString.builder()
                                                                                .length(SizeLength.from(1))
                                                                                .characterSet(StandardCharsets.UTF_8)
                                                                                .build())
                                                                        .build())
                                                                .field(SchemaTypeFieldNamed.builder()
                                                                        .name("hash")
                                                                        .type(SchemaTypeString.builder()
                                                                                .length(SizeLength.from(1))
                                                                                .characterSet(StandardCharsets.UTF_8)
                                                                                .build())
                                                                        .build())
                                                                .build())
                                                        .build())
                                                .build()))
                                .build())
                        .build())
                .build();

        assertTrue(expectedMintFuncParams.equals(mintFuncParams));
    }

    @SneakyThrows
    @Test
    public void shouldParseV0Schema() {
        final ModuleSchema schema = new SchemaParser().parse(SCHEMA_BYTES);
        assertEquals(SchemaVersion.V0, schema.getVersion());

        val contract = schema.getContract("notary");
        assertNotNull(contract);

        val registerFileFn = contract.getReceiveFunction("registerfile");
        assertNotNull(registerFileFn);

        val fnParams = registerFileFn.getParameter().get();
        assertTrue(fnParams.equals(SchemaTypeString.builder()
                .length(SizeLength.from(2))
                .characterSet(StandardCharsets.UTF_8)
                .build()));
    }
}
