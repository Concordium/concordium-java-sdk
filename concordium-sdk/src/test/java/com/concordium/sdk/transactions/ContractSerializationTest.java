package com.concordium.sdk.transactions;

import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.schema.*;
import com.concordium.sdk.types.Map;
import com.concordium.sdk.types.UInt32;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;

import static java.nio.file.Files.readAllBytes;
import static org.junit.Assert.*;

public class ContractSerializationTest {
    public static byte[] EXPECTED_CIS2_MINT_FN_PARAM_BYTES = Base64.getDecoder()
            .decode("AGGIL5BLvgAW3iIw+bPXPIDqB2wWBGmTxP8Cf2RhqBzYAQQAAAABCwBleGFtcGxlLmNvbUAANWZkNmU3OTVkNzQzOG" +
                    "Y0MTQzMzI5ZjZmMjQxNTI0ZGRjZTMzNzcxZmNkZDIzY2I3Y2IxMjA5MzY3YTRkZDA0NQ==");

    private static byte[] CIS2_V2_SCHEMA_BYTES;

    static {
        try {
            CIS2_V2_SCHEMA_BYTES = readAllBytes(Paths.get("src/test/java/com/concordium/sdk/binaries/schema.bin.v2"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    @Test
    public void shouldSerializeV2Cis2MintParameter() {
        // Construct Java Parameter Java Types
        Cis2MintParams param = Cis2MintParams.builder()
                .owner(Cis2Address.from(AccountAddress.from("3ghVzRz9BbQbQhetPFFK9JGZXeWV6fkM8K64NWz8VkgHynxkoA")))
                .tokens(new Map<>(Arrays.asList(new Cis2MetadataItem(
                        Cis2ContractTokenId.from(UInt32.from(1)),
                        Cis2TokenMetadata.builder()
                                .url("example.com")
                                .hash("5fd6e795d7438f4143329f6f241524ddce33771fcdd23cb7cb1209367a4dd045")
                                .build()))))
                .build();

        val paramJson = JsonMapper.INSTANCE.writeValueAsString(param);

        val paramBytes = new SchemaParser()
                .parse(CIS2_V2_SCHEMA_BYTES)
                .getContract("CIS2-NFT")
                .getReceiveFunction("mint")
                .getParameter()
                .get()
                .jsonToBytes(JsonMapper.INSTANCE.readTree(paramJson));

        assertArrayEquals(EXPECTED_CIS2_MINT_FN_PARAM_BYTES, paramBytes);
    }

    @Test
    @SneakyThrows
    public void shouldDeserializeV2Cis2ViewStateReturn() {
        val bytes = ByteBuffer.wrap(Hex.decodeHex("070000000012625238c6723490a2a7dc30ec114ad7de6d2470ddba79b5a3026523c09185" +
                "3902000000040000000504000000060100000001d80300000000000000000000000000000061882f904bbe0016de2230f9b3d7" +
                "3c80ea076c16046993c4ff027f6461a81cd800000000010000000195040000000000000000000000000000007757d55f7ce46c" +
                "eed2b0318b4639fb6ce25d1c61a49c7a2c7855b365efaab23001000000040000000200000000009d230671ab6efaf2861f0b59" +
                "42e650186036b8fbb4e9973f5634b43e664d3b4b0400000004000000070400000008040000000904000000100100000001d803" +
                "000000000000000000000000000000a057ae33fe266bc432a762c4a6da4834d1f88e36636b83893d5e7f2932a1090000000000" +
                "0100000001d803000000000000000000000000000000c6fc03f34c29e347896db49e68467c303f15b9d898572b4fbe82b062787" +
                "633470100000004000000040000000000effed184d080331dc979d6d026fb47d46b167664b075786dda4e3c1e3ad5e40302000" +
                "00004000000010400000003000000000a00000004000000010400000002040000000304000000040400000005040000000604" +
                "000000070400000008040000000904000000100a0000000400000001670068747470733a2f2f697066732e696f2f697066732" +
                "f516d4e746d6f7363683332777047586858767053744d734d396f72424256465744383846366b525964656738384b3f66696c6" +
                "56e616d653d6e66745f30303030303030315f6d657461646174612e6a736f6e40006564376139363664346361376462663765" +
                "333632616532376161616636373535613933303633613765313664633564633666373032346637356337626439313204000000" +
                "02670068747470733a2f2f697066732e696f2f697066732f516d5337336647795047573978626a5475614a6d414e445a636832" +
                "457632676878704d6656316544434b4e7237583f66696c656e616d653d6e66745f30303030303030325f6d657461646174612e" +
                "6a736f6e4000663830366431616335356530326364393739646333623931306131626136353039663632363934383033373164" +
                "323334623531323265383535353931343662650400000003670068747470733a2f2f697066732e696f2f697066732f516d5373" +
                "517441387a7861524a383966556455706d6f45466a506a4159356a6e324c357a7a5471436b7145624b713f66696c656e616d65" +
                "3d6e66745f30303030303030335f6d657461646174612e6a736f6e400039626438383835316365383632373066336464313966" +
                "626533653537623733653439656532633962326265346536336535383565616132323261323165663133040000000467006874" +
                "7470733a2f2f697066732e696f2f697066732f516d57354537486375627a78635a67414e3756544d7470625959327342517556" +
                "757644554a4e506d5a68775131343f66696c656e616d653d6e66745f30303030303030345f6d657461646174612e6a736f6e40" +
                "003438346639663865393531613530323334383230383863633661333938636538393536316634383766303832623666333862" +
                "66346334646365393861383435630400000005670068747470733a2f2f697066732e696f2f697066732f516d58596451543670" +
                "43594b42746e4a334c397874747a7135475a6f457256336f396a455932387637694c734d343f66696c656e616d653d6e66745f" +
                "30303030303030355f6d657461646174612e6a736f6e4000326538356661396666326431626331396137363430616564643139" +
                "623165316236356332303436326139633061383733363935646566633835353431336131620400000006670068747470733a2f" +
                "2f697066732e696f2f697066732f516d556a33346f4b5767567457674a33346a5a3979735577744644684a3161634b74454b52" +
                "627448696f7276744c3f66696c656e616d653d6e66745f30303030303030365f6d657461646174612e6a736f6e400037393161" +
                "6130356664373030633139333334663033363331623631643530666364323433363264303165373335333763366562333462666" +
                "332373234336566640400000007430068747470733a2f2f697066732e696f2f697066732f516d64744e675839755a4d6f38616" +
                "750735355724b77655161595a35517964487a446a356e6f57525451617863484000346234363932313838323332373565376330" +
                "3339393862623364613764663333393835336331646161396135303765636535303232343166343235616565303504000000084" +
                "30068747470733a2f2f697066732e696f2f697066732f516d5243466a5475416d59795a427a6953756e504c79796d466e535165" +
                "313348717132394d746b5956396f51655a400064396365303232666234613230663636616163336530623630633464376161613" +
                "5336165323734366635396532636162393930336435653739316231333138650400000009430068747470733a2f2f697066732e" +
                "696f2f697066732f516d4e6674384c705764597563744a7a563161375a42356a4a5473766d774c3831617a776764626a6a7942" +
                "72677a400035646666306236356636356462343430636430366634623337333163343633363736326136333830613663353466363963356234" +
                "3236393634663538383234370400000010430068747470733a2f2f697066732e696f2f697066732f516d63746e4d327968456f535562" +
                "4a735863487262676b5470394c6d6e447665794a41697859486847475657416240003061656538306536623533326162623661" +
                "3230656262643938383361646464333834386435336331636165393838353266666234353837383736363563363562"));

        final ModuleSchema schema = new SchemaParser().parse(CIS2_V2_SCHEMA_BYTES);
        val contract = schema.getContract("CIS2-NFT");
        val viewFn = contract.getReceiveFunction("view");
        assertNotNull(viewFn);

        val returnType = viewFn.getReturnValue();
        assertTrue(returnType.isPresent());

        val json = returnType.get().bytesToJson(bytes);
        assertNotNull(json);

        val viewState = JsonMapper.INSTANCE.readValue(json.toString(), Cis2ViewState.class);
        assertNotNull(viewState);

        val expectedViewState = Cis2ViewState.builder()
                .stateItem(Cis2StateItem.builder()
                        .address(Cis2Address.from(AccountAddress.from("35qmpUFbhiTD4sRvnFuFMm31xLmptsbwQkSPPTxnyHTmRziimB")))
                        .state(Cis2ViewAddressState.builder()
                                .operator(Cis2Address.from(ContractAddress.from(984, 0)))
                                .ownedToken(Cis2ContractTokenId.from("00000005"))
                                .ownedToken(Cis2ContractTokenId.from("00000006"))
                                .build())
                        .build())
                .stateItem(Cis2StateItem.builder()
                        .address(Cis2Address.from(AccountAddress.from("3ghVzRz9BbQbQhetPFFK9JGZXeWV6fkM8K64NWz8VkgHynxkoA")))
                        .state(Cis2ViewAddressState.builder()
                                .operator(Cis2Address.from(ContractAddress.from(1173, 0)))
                                .ownedTokens(Arrays.asList())
                                .build())
                        .build())
                .stateItem(Cis2StateItem.builder()
                        .address(Cis2Address.from(AccountAddress.from("3rJdsYZ6KVmZM96zX8LakBcsDyrLyUP4sWCsbP74cYTdMRSGVi")))
                        .state(Cis2ViewAddressState.builder()
                                .operators(Arrays.asList())
                                .ownedToken(Cis2ContractTokenId.from("00000002"))
                                .build())
                        .build())
                .stateItem(Cis2StateItem.builder()
                        .address(Cis2Address.from(AccountAddress.from("48x2Uo8xCMMxwGuSQnwbqjzKtVqK5MaUud4vG7QEUgDmYkV85e")))
                        .state(Cis2ViewAddressState.builder()
                                .operator(Cis2Address.from(ContractAddress.from(984, 0)))
                                .ownedToken(Cis2ContractTokenId.from("00000007"))
                                .ownedToken(Cis2ContractTokenId.from("00000008"))
                                .ownedToken(Cis2ContractTokenId.from("00000009"))
                                .ownedToken(Cis2ContractTokenId.from("00000010"))
                                .build())
                        .build())
                .stateItem(Cis2StateItem.builder()
                        .address(Cis2Address.from(AccountAddress.from("4AMuoepPZZqSxmgEZadr1bRjYVGGmQAmdp95RAgy9MDSucdYnR")))
                        .state(Cis2ViewAddressState.builder()
                                .operator(Cis2Address.from(ContractAddress.from(984, 0)))
                                .ownedTokens(Arrays.asList())
                                .build())
                        .build())
                .stateItem(Cis2StateItem.builder()
                        .address(Cis2Address.from(AccountAddress.from("4TNy58eMcJRsdqPz31HEaiYGzPBrQwKeDmMPAwqimFKD22AK2x")))
                        .state(Cis2ViewAddressState.builder()
                                .operators(Arrays.asList())
                                .ownedToken(Cis2ContractTokenId.from("00000004"))
                                .build())
                        .build())
                .stateItem(Cis2StateItem.builder()
                        .address(Cis2Address.from(AccountAddress.from("4mSY7TFuMFj7RNHTKKWQXXsjQXv9Y3LtwWGQZGtdwk8ufgquGy")))
                        .state(Cis2ViewAddressState.builder()
                                .operators(Arrays.asList())
                                .ownedToken(Cis2ContractTokenId.from("00000001"))
                                .ownedToken(Cis2ContractTokenId.from("00000003"))
                                .build())
                        .build())
                .token(Cis2ContractTokenId.from("00000001"))
                .token(Cis2ContractTokenId.from("00000002"))
                .token(Cis2ContractTokenId.from("00000003"))
                .token(Cis2ContractTokenId.from("00000004"))
                .token(Cis2ContractTokenId.from("00000005"))
                .token(Cis2ContractTokenId.from("00000006"))
                .token(Cis2ContractTokenId.from("00000007"))
                .token(Cis2ContractTokenId.from("00000008"))
                .token(Cis2ContractTokenId.from("00000008"))
                .token(Cis2ContractTokenId.from("00000010"))
                .metadataItem(Cis2MetadataItem.builder()
                        .tokenId(Cis2ContractTokenId.from("00000001"))
                        .tokenMetadata(Cis2TokenMetadata.builder()
                                .url("https://ipfs.io/ipfs/QmNtmosch32wpGXhXvpStMsM9orBBVFWD88F6kRYdeg88K?filename=nft_00000001_metadata.json")
                                .hash("ed7a966d4ca7dbf7e362ae27aaaf6755a93063a7e16dc5dc6f7024f75c7bd912")
                                .build())
                        .build())
                .metadataItem(Cis2MetadataItem.builder()
                        .tokenId(Cis2ContractTokenId.from("00000002"))
                        .tokenMetadata(Cis2TokenMetadata.builder()
                                .url("https://ipfs.io/ipfs/QmS73fGyPGW9xbjTuaJmANDZch2Ev2ghxpMfV1eDCKNr7X?filename=nft_00000002_metadata.json")
                                .hash("f806d1ac55e02cd979dc3b910a1ba6509f6269480371d234b5122e85559146be")
                                .build())
                        .build())
                .metadataItem(Cis2MetadataItem.builder()
                        .tokenId(Cis2ContractTokenId.from("00000003"))
                        .tokenMetadata(Cis2TokenMetadata.builder()
                                .url("https://ipfs.io/ipfs/QmSsQtA8zxaRJ89fUdUpmoEFjPjAY5jn2L5zzTqCkqEbKq?filename=nft_00000003_metadata.json")
                                .hash("9bd88851ce86270f3dd19fbe3e57b73e49ee2c9b2be4e63e585eaa222a21ef13")
                                .build())
                        .build())
                .metadataItem(Cis2MetadataItem.builder()
                        .tokenId(Cis2ContractTokenId.from("00000004"))
                        .tokenMetadata(Cis2TokenMetadata.builder()
                                .url("https://ipfs.io/ipfs/QmW5E7HcubzxcZgAN7VTMtpbYY2sBQuVuvDUJNPmZhwQ14?filename=nft_00000004_metadata.json")
                                .hash("484f9f8e951a5023482088cc6a398ce89561f487f082b6f38bf4c4dce98a845c")
                                .build())
                        .build())
                .metadataItem(Cis2MetadataItem.builder()
                        .tokenId(Cis2ContractTokenId.from("00000005"))
                        .tokenMetadata(Cis2TokenMetadata.builder()
                                .url("https://ipfs.io/ipfs/QmXYdQT6pCYKBtnJ3L9xttzq5GZoErV3o9jEY28v7iLsM4?filename=nft_00000005_metadata.json")
                                .hash("2e85fa9ff2d1bc19a7640aedd19b1e1b65c20462a9c0a873695defc855413a1b")
                                .build())
                        .build())
                .metadataItem(Cis2MetadataItem.builder()
                        .tokenId(Cis2ContractTokenId.from("00000006"))
                        .tokenMetadata(Cis2TokenMetadata.builder()
                                .url("https://ipfs.io/ipfs/QmUj34oKWgVtWgJ34jZ9ysUwtFDhJ1acKtEKRbtHiorvtL?filename=nft_00000006_metadata.json")
                                .hash("791aa05fd700c19334f03631b61d50fcd24362d01e73537c6eb34bfc27243efd")
                                .build())
                        .build())
                .metadataItem(Cis2MetadataItem.builder()
                        .tokenId(Cis2ContractTokenId.from("00000007"))
                        .tokenMetadata(Cis2TokenMetadata.builder()
                                .url("https://ipfs.io/ipfs/QmdtNgX9uZMo8agPsSUrKweQaYZ5QydHzDj5noWRTQaxcH")
                                .hash("4b469218823275e7c03998bb3da7df339853c1daa9a507ece502241f425aee05")
                                .build())
                        .build())
                .metadataItem(Cis2MetadataItem.builder()
                        .tokenId(Cis2ContractTokenId.from("00000008"))
                        .tokenMetadata(Cis2TokenMetadata.builder()
                                .url("https://ipfs.io/ipfs/QmRCFjTuAmYyZBziSunPLyymFnSQe13Hqq29MtkYV9oQeZ")
                                .hash("d9ce022fb4a20f66aac3e0b60c4d7aaa53ae2746f59e2cab9903d5e791b1318e")
                                .build())
                        .build())
                .metadataItem(Cis2MetadataItem.builder()
                        .tokenId(Cis2ContractTokenId.from("00000009"))
                        .tokenMetadata(Cis2TokenMetadata.builder()
                                .url("https://ipfs.io/ipfs/QmNft8LpWdYuctJzV1a7ZB5jJTsvmwL81azwgdbjjyBrgz")
                                .hash("5dff0b65f65db440cd06f4b3731c4636762a6380a6c54f69c5b426964f588247")
                                .build())
                        .build())
                .metadataItem(Cis2MetadataItem.builder()
                        .tokenId(Cis2ContractTokenId.from("00000010"))
                        .tokenMetadata(Cis2TokenMetadata.builder()
                                .url("https://ipfs.io/ipfs/QmctnM2yhEoSUbJsXcHrbgkTp9LmnDveyJAixYHhGGVWAb")
                                .hash("0aee80e6b532abb6a20ebbd9883addd3848d53c1cae98852ffb458787665c65b")
                                .build())
                        .build())
                .build();

        assertTrue(expectedViewState.equals(viewState));
    }
}
