package com.concordium.sdk.transactions.schema;

import com.concordium.sdk.types.UInt;
import com.concordium.sdk.types.UInt16;
import com.concordium.sdk.types.UInt32;
import com.google.common.collect.ImmutableList;
import lombok.val;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class SchemaParser {

    private final SchemaTypeUInt schemaTypeUInt = new SchemaTypeUInt();
    private final SchemaTypeUInt16 schemaTypeUInt16 = new SchemaTypeUInt16();
    private final SchemaTypeUInt32 schemaTypeUInt32 = new SchemaTypeUInt32();
    private final SchemaTypeBool schemaTypeBool = new SchemaTypeBool();
    private final SchemaTypeString schemaTypeString = new SchemaTypeString(
            SizeLength.from(2), StandardCharsets.UTF_8);

    /**
     * Parses input Schema {@link Byte} Array into {@link ModuleSchema}
     *
     * @param schemaBytes {@link Byte} Array of Schema
     * @return Parsed {@link ModuleSchema}
     */
    public ModuleSchema parse(byte[] schemaBytes) {
        val buff = ByteBuffer.wrap(schemaBytes);
        val magicHash = readUInt16(buff);

        if (magicHash.getValue() == UInt16.MAX_VALUE) {
            // Versioned Schema
            val version = readUInt(buff);
            switch (version.getValue()) {
                case 0:
                    val contractsV0 = readStringKeyMap(buff, (buff2) -> readContractV0(buff2));
                    return new ModuleSchemaV0(contractsV0);
                case 1:
                    val contractsV1 = readStringKeyMap(buff, (buff2) -> readContractV1(buff2));
                    return new ModuleSchemaV1(contractsV1);
                case 2:
                    val contractsV2 = readStringKeyMap(buff, (buff2) -> readContractV2(buff2));
                    return new ModuleSchemaV2(contractsV2);
                default:
                    throw new RuntimeException("Invalid Version");
            }
        }

        buff.position(0);
        val contractsV0 = readStringKeyMap(buff, (buff2) -> readContractV0(buff2));

        return new ModuleSchemaV0(contractsV0);
    }

    private ContractV2 readContractV2(ByteBuffer buff) {
        val init = readOptional(buff, (buff2) -> readFunctionV2(buff2));
        val receive = readStringKeyMap(buff, (buff2) -> readFunctionV2(buff2));

        return ContractV2.builder()
                .init(init)
                .receive(receive)
                .build();
    }

    private ContractFunctionV2 readFunctionV2(ByteBuffer buff) {
        val builder = ContractFunctionV2.builder();
        val idx = this.readUInt(buff).getValue();

        switch (idx) {
            case 0:
                return builder
                        .parameter(Optional.of(readSchemaType(buff)))
                        .returnValue(Optional.empty())
                        .error(Optional.empty())
                        .build();
            case 1:
                return builder
                        .parameter(Optional.empty())
                        .returnValue(Optional.of(readSchemaType(buff)))
                        .error(Optional.empty())
                        .build();
            case 2:
                return builder
                        .parameter(Optional.of(readSchemaType(buff)))
                        .returnValue(Optional.of(readSchemaType(buff)))
                        .error(Optional.empty())
                        .build();
            case 3:
                return builder
                        .parameter(Optional.empty())
                        .returnValue(Optional.empty())
                        .error(Optional.of(readSchemaType(buff)))
                        .build();
            case 4:
                return builder
                        .parameter(Optional.of(readSchemaType(buff)))
                        .returnValue(Optional.empty())
                        .error(Optional.of(readSchemaType(buff)))
                        .build();
            case 5:
                return builder
                        .parameter(Optional.empty())
                        .returnValue(Optional.of(readSchemaType(buff)))
                        .error(Optional.of(readSchemaType(buff)))
                        .build();
            case 6:
                return builder
                        .parameter(Optional.of(readSchemaType(buff)))
                        .returnValue(Optional.of(readSchemaType(buff)))
                        .error(Optional.of(readSchemaType(buff)))
                        .build();
            default:
                throw new RuntimeException("Invalid function v2 type");
        }
    }

    private ContractV0 readContractV0(final ByteBuffer buff) {
        return ContractV0.builder()
                .state(readOptional(buff, buff2 -> readSchemaType(buff2)))
                .init(readOptional(buff, buff2 -> readFunctionV0(buff2)))
                .receive(readStringKeyMap(buff, buff2 -> readFunctionV0(buff2)))
                .build();
    }

    private ContractFunctionV0 readFunctionV0(ByteBuffer buff) {
        return ContractFunctionV0.builder()
                .parameter(Optional.of(readSchemaType(buff)))
                .build();
    }

    private ContractV1 readContractV1(ByteBuffer buff) {
        return ContractV1.builder()
                .init(readOptional(buff, (buff2) -> readFunctionV1(buff2)))
                .receive(readStringKeyMap(buff, (buff2) -> readFunctionV1(buff2)))
                .build();
    }

    private ContractFunctionV1 readFunctionV1(ByteBuffer buff) {
        val builder = ContractFunctionV1.builder();
        val idx = this.readUInt(buff).getValue();

        switch (idx) {
            case 0:
                return builder
                        .parameter(Optional.of(readSchemaType(buff)))
                        .returnValue(Optional.empty())
                        .build();
            case 1:
                return builder
                        .parameter(Optional.empty())
                        .returnValue(Optional.of(readSchemaType(buff)))
                        .build();
            case 2:
                return builder
                        .parameter(Optional.of(readSchemaType(buff)))
                        .returnValue(Optional.of(readSchemaType(buff)))
                        .build();
            default:
                throw new RuntimeException("Invalid function v1 type");
        }
    }

    private SchemaType readSchemaType(ByteBuffer buff) {
        val idx = this.readUInt(buff).getValue();

        switch (idx) {
            case 0:
                return new SchemaTypeUnit();
            case 1:
                return new SchemaTypeBool();
            case 2:
                return new SchemaTypeUInt();
            case 3:
                return new SchemaTypeUInt16();
            case 4:
                return new SchemaTypeUInt32();
            case 5:
                return new SchemaTypeUInt64();
            case 6:
                return new SchemaTypeInt8();
            case 7:
                return new SchemaTypeInt16();
            case 8:
                return new SchemaTypeInt32();
            case 9:
                return new SchemaTypeInt64();
            case 10:
                return new SchemaTypeAmount();
            case 11:
                return new SchemaTypeAccountAddress();
            case 12:
                return new SchemaTypeContractAddress();
            case 13:
                return new SchemaTypeTimestamp();
            case 14:
                return new SchemaTypeDuration();
            case 15:
                return new SchemaTypeTuple(readSchemaType(buff), readSchemaType(buff));
            case 16:
                return new SchemaTypeList(readSizeLength(buff), readSchemaType(buff));
            case 17:
                return new SchemaTypeSet(readSizeLength(buff), readSchemaType(buff));
            case 18:
                return new SchemaTypeMap(
                        readSizeLength(buff),
                        new SchemaTypeTuple(readSchemaType(buff), readSchemaType(buff)));
            case 19:
                return new SchemaTypeArray(readUInt32(buff).getValue(), readSchemaType(buff));
            case 20:
                return new SchemaTypeStruct(readSchemaTypeFields(buff));
            case 21:
                return new SchemaTypeEnum(readVector(buff, (buff2) -> readSchemaTypeEnumVariant(buff2)));
            case 22:
                return new SchemaTypeString(readSizeLength(buff), StandardCharsets.UTF_8);
            case 23:
                return new SchemaTypeUInt128();
            case 24:
                return new SchemaTypeInt128();
            case 25:
                return new SchemaTypeContractName(readSizeLength(buff));
            case 26:
                return new SchemaTypeReceiveName(readSizeLength(buff));
            case 27:
                return new SchemaTypeULeb128(readUInt32(buff).getValue());
            case 28:
                return new SchemaTypeILeb128(readUInt32(buff).getValue());
            case 29:
                return new SchemaTypeByteList(readSizeLength(buff));
            case 30:
                return new SchemaTypeByteArray(readUInt32(buff).getValue());
            default:
                throw new RuntimeException("invalid idx for SchemaType");
        }
    }

    private SchemaTypeEnumVariant readSchemaTypeEnumVariant(ByteBuffer buff) {
        return new SchemaTypeEnumVariant(readString(buff), readSchemaTypeFields(buff));
    }

    private SchemaTypeFields readSchemaTypeFields(ByteBuffer buff) {
        val idx = readUInt(buff).getValue();

        switch (idx) {
            case 0:
                final ImmutableList<SchemaTypeFieldNamed> fieldsNamed = readVector(buff, (buff2) -> readSchemaTypeFieldNamed(buff2));
                return new SchemaTypeFieldsNamed(fieldsNamed);
            case 1:
                final ImmutableList<SchemaType> fieldsUnNamed = readVector(buff, (buff2) -> readSchemaType(buff2));
                return new SchemaTypeFieldsUnNamed(fieldsUnNamed);
            case 2:
                return new SchemaTypeFieldsNone();
            default:
                throw new RuntimeException("Invalid idx: " + idx + " for SchemaTypeFields");
        }
    }

    private SchemaTypeFieldNamed readSchemaTypeFieldNamed(ByteBuffer buff) {
        return new SchemaTypeFieldNamed(readString(buff), readSchemaType(buff));
    }

    private <T> ImmutableList<T> readVector(ByteBuffer buff, Function<ByteBuffer, T> typeParser) {
        val length = readUInt32(buff);
        val ret = ImmutableList.<T>builder();

        for (int i = 0; i < length.getValue(); i++) {
            ret.add(typeParser.apply(buff));
        }

        return ret.build();
    }

    private <TValue> Map<String, TValue> readStringKeyMap(
            final ByteBuffer buff,
            Function<ByteBuffer, TValue> valueParser) {
        val length = this.readUInt32(buff);
        Map<String, TValue> map = new HashMap<>();

        for (int i = 0; i < length.getValue(); i++) {
            val key = this.readString(buff);
            val value = valueParser.apply(buff);
            map.put(key, value);
        }

        return map;
    }

    private SizeLength readSizeLength(ByteBuffer buff) {
        return SizeLength.from(readUInt(buff));
    }

    private <T> Optional<T> readOptional(ByteBuffer buff, Function<ByteBuffer, T> typeParser) {
        return readBool(buff) ? Optional.of(typeParser.apply(buff)) : Optional.empty();
    }

    private boolean readBool(ByteBuffer buff) {
        return schemaTypeBool.fromSchemaBytes(buff).getValue();
    }

    private String readString(final ByteBuffer buff) {
        return schemaTypeString.fromSchemaBytes(buff);
    }

    private UInt readUInt(final ByteBuffer buff) {
        return schemaTypeUInt.fromSchemaBytes(buff);
    }

    private UInt16 readUInt16(final ByteBuffer buff) {
        return schemaTypeUInt16.fromSchemaBytes(buff);
    }

    private UInt32 readUInt32(final ByteBuffer buff) {
        return schemaTypeUInt32.fromSchemaBytes(buff);
    }
}
