package com.concordium.sdk.queries;

import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.concordium.sdk.crypto.pointchevalsanders.PSPublicKey;
import com.concordium.sdk.responses.blocksummary.updates.queues.Description;
import com.concordium.sdk.responses.blocksummary.updates.queues.IdentityProviderInfo;
import concordium.ConcordiumP2PRpc;
import lombok.val;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class GetIdentityProvidersTest {

    @Test
    public void testShouldDeserializeJson() {
        val psPublicKey = "97f1d3a73197d7942695638c4fa9ac0fc3688c4f9774b905a14e3a3f171bac586c55e83ff97a1a" +
                "effb3af00adb22c6bb93e02b6052719f607dacd3a088274f65596bd0d09920b61ab5da61bbdc7f5049334cf112139" +
                "45d57e5ac7d055d042b7e024aa2b2f08f0a91260805272dc51051c6e47ad4fa403b02b4510b647ae3d1770bac0326" +
                "a805bbefd48056c8c121bdb80000001e90ba8909d0806c237daf3c4d533ee12c3c279fa9dba217207c14ce949" +
                "b67ec11723e1768b4c8d8d4300a4e7e2eed8abcb0862bdb11f5ad4a28d0a3fa0e85b689a994c417f5a306a30503" +
                "70052b4a56aff95349a2c97f749a68085c77acbf1df399d3603227f82698010dee3cecb6855f27edc0fbc9d183a90" +
                "9835bf53a79ad6771aad9950d101940db4012776a06b44596134ae37e01aea98f2333083d4847e8dbf585c835e1d2" +
                "4072681933093fd152f8524eddc85554e1f5f5d463ed3f4c06b5d11c1d79124ab6f89cc556f44659e46590936941a" +
                "461941e934d26354ef1c1dc7ca53175305b19adcf274a7493dccba69bd98ade4ea154f028f96d73bce9f9dbae14f6" +
                "cc44e164d0cf4ebc850daf28f0514d8d11526495277066982c33d13dad43482806148cb2c8015637540ca0649c22d" +
                "e2fc3e11bbb015ce2df7ae3cd845b3eed46c50ba4beeaf9afb7834be1138eff0f0d2c991b33e4fd9eda1605f22b2e" +
                "c492fbdc3ea4f88d66e837f044a10f3abd1edc6f54087cf2589215bafaf4e487d280b2b0e1bb4452dcfa5888a2f8e" +
                "ecd243180ceac2eba4f24b79c2c56c93459a8ee94b4e69a0ccea43def0ef34628acb418315867594ed1179d4e349dd" +
                "c2b2b4e44f016a93152fe07bb87b7c2737db33b1974cc1559245e96df657abd22bcab4673a711974eebe760032096" +
                "ee79865bb6b5abaaca2661044ef058aee26ce1cb79bdcdfeeb0b6ea47e731957b5b71ca9b30fcfcf84317351ba9a" +
                "520f62695b688fc2125c693125c3f07d82536feb477d721eb6ee30eefdaee074434a1de3e7a40b03acf9b42fe187" +
                "9e7b1411990a08227889821acf06b88c7e086028f1af2adcbaa74eb8936f665b3fd011e08ba7dd888ec898f1011" +
                "4b4659610d1409a64112ee972b7b1f089c1c9cc3be74e6b08a761ee18d0bdd063c4b6327123e0bf9e8dad20dc8fb" +
                "048e06caf87adf3fda5fabbe8398394051765b7194b28b74280f9d7736a2fdcae133cc29f0466c6edc06df087ed2" +
                "9ab703dd598183b75bbed6b0a71947fe8a18fdcddcd49386558911b1c1362e5b8159ae39192e791e725432154559" +
                "5abd9ad7e859e80ae3dddb14731a981599f8e7fef5bb77151850fe343f7dd91db35cd00c1d02e7101c5dbfee9d956" +
                "2e9200ce13e7638a5a92a723299986d22e9699a90c15f26151417ad770c8534a1b54f29dd3b64dc2a8f0237433734" +
                "4e5aca3d292364ee245476def3fb704f8bc065a7a1a6a79ae2387bfb80871460d9f3052e85f7ce042c10432580043" +
                "3728da6f84790b049df5933a59b3c72485bda556b3360efc0d02c91abf672284bf7c77d0638667e13a3dbe17d27933" +
                "95009983fba2902e7862de908d22f5562eee6b5f7cc59f10a1afa934a3510f4b032f136c78a56dc5d8c11320088084" +
                "73be726b698a91c0030468b64366026ed779d1a0e9d65508ac22f5800406f084c7fb4b04ab1c4af3dfadb9e51cd765" +
                "0fcdac64ac2f8d8f8f9e483e8c44b9fa93afcf36c2210110cf04b3f154069abc50df6d8bf6a59da226fc7b329b5fbc" +
                "921ec66d87159665b9b25ca49712f9067a8dd901342d97aad36a0129336b9a0e51d1174844cd719163266c8b304008" +
                "86600e9ee9804c4813b4f03072f0a4dd57915f15ef4d8208e609b0d08126f6aa95b97096c96a0b125f097874dd2fb8" +
                "428b930211575f316cabad5f14432b57d7342f883b120bf2a134c36d45960a9a9a68b8fac820f03e209d48197d467" +
                "c416fbfd764868c63770a038333542835ad8db288a9f99389e455fe1ccb74a53b00a6665bfc206a9da0c15fb8b588f" +
                "504ec9675bbd75a223f358545b8d93f31b6be902408000476ed980e7a5e9b13046353342e3081dba9ae0af25e6ac3e" +
                "0130db1714a2fba6fab008513b4ac1a285c10274cbadab17eb6fa27125f5c3c3752a8ae7f39cd943a7dfa7ebb181cd" +
                "300a3dc6a69d11591b91d28eeeca9891035693e0b71472015cd1754deed74de61c04982153417c3cd0b060632c8fa" +
                "f7c3dfd6591867dae68923f9a5f3d8190000001e93caae1dc016ba6b674e97ec58366e5d9bbc91020c734f15b9bd94" +
                "54a53f50d081f4301d80c09b30a0515111a1089db200e59fe3377513e0f2a07d7059854854b9d7ad2febc90154d41" +
                "91f235e1db4e3fa0690312346c407a960b18723ac568f8ef3b09c3d8574fe3a118815ff80391b71145f822cec0921" +
                "006a3e2e248e75f4077a5899eb5c94594ecf5d53c9461e5d0697fb336d8463c9c7cafbb5d2f59f4dfe3fa3e93cf1a" +
                "f6bfef4b862e2b3bf6a29f456025829b491b393fea7bb7859e4ab776ecf95abd7fa1e4e03c77525af58d297be183a" +
                "9437710f72b35d7fe18c9daae5a0b0dedec5d91eaa1cf779044bd304e9b5c3ab9ee20d197a057a328a71326e9301d" +
                "f60c729ac23f4280245c262de278cbd64c9c58e9591044943c20dc51f8c072f4121098bb8949ad7b8b042b98daba" +
                "dfdee95a825f106690a736f46d518a7db5ba13f5dc6a3fe96eecb5c9f138919ab329cca7582e448db8d51fc30528e" +
                "fde67a4de4ceb1df40d4c3248bf1bb6618a0411dc205e67f192727a82784cd77839b3f91e1c0b96858187c931d78a7" +
                "9ea1e8e63924171d440eb117d9e5696dd9182a24d66c1fc885171901016f92a23a136b07b8033cb163821ea2ceb691" +
                "7f36a17fde1bf17fd299f0bcc3021f012c6bbcff17df0e6bca3a0aaee4f298161ef78a6de30c0b14bf3623d8fb980e" +
                "02610d31ec22ce6449f19df75acac7671f17ca75529c118215f3a7ed450ec71e7175c013de7a423cd96425d10024b6" +
                "470b7b3e90ffd8ed12a19bd0dc61be354fbdfa2b6f42cf7fc427eedff7d90d432d3fa2fca09a13d0b855f0824c081b" +
                "8f3010e53deabfc255ff6be14e82778e8aafe91eb222668b6352a0761e424e16675b95cd15023ee7a1927e61540c19" +
                "f32177a7b564834df3ba08f6015b85f3abfd3589a88a6963771f7449137d8d58a98ab1411abdb23196352eca8d820c" +
                "df41a9bf4690e8dc37e683a5739d82a933d1295797ea30d0cf5efca3a26fa1eb4c477830ba93d9069eb7477419ab1d" +
                "efc6d57deee580bd707388f971e9a6359ed03b6bbed63ac29f765266ccf6f74631b60088c4d9415eb759611ea07df1" +
                "21917596067e74558180c9d7f77cbb571a29bdc892a1d8431b195e3c8cce609a221a2fea32cf948d5c0aeb37e3a1f" +
                "1e01668832b1e6dc21435926f28037c079b721115556c5eafe1da66ca9ccb63233fa3e2bd0fbb4718b2d8a904f05a" +
                "775d60f9f6e7c2891078883306208ebc8bab4c61961707bff16b1ba070e115dd068837591b1d689547e80ea710a95" +
                "4ff5f8a0db3f6fcc06c50e47df20e837cf793f67bc8dd66a060e0030c93b5e1190e7e934e2f85cdc06fbf5728ba85" +
                "b82aa80eac192d900a4f4881352756e3a86dc9164c82aec566d7dfcb7d6f61d6a6cbf5959b2dc0b5eea15874c9d7c6" +
                "19e227f94fcb235ab786bbab5680500615136434e52cec54cf0568cc57e8191e9379607878e8925e5774dc3b6ecd36" +
                "b7b1ce6061719fc4649c4457b8a73b1de11df3f475d7d763c6e31e6f1311273f3dd007b5c36ae9641bc1c1287748f9f" +
                "27a0a3728c90e2ee092c747b96fe49f2e4b438d14dad21cfee9ef30c4d29365ea7c045bd4e1ead7d9a78525d33c9d3" +
                "ffe81b81016349ab583633a3343f73a56da136996d1fab654f3f8143ce854367a9958e7c7744bb6d3ec0f644dc11aa" +
                "2e9b91f04e70d89202fc66ed4567e583e0729dd453ce8284c9b077864f2596cbae38c455c492ff8353df4db5891e18" +
                "ac5fb1825ada5b80e7ffd90ced8b9395149e5d96731438ccaeee7bfd05cce5c9e220f96eef7e15eed4b5665f94ec7e" +
                "6b22e24bbdc0509be51a5128f5644264641a88a8949038d6bffc860902874d29495ac9c11dae076eda76c541937d07" +
                "b51f5b56623f6918d4b11dc2751b964062014827cb8480d82bc7802b29b3c86aab59e0ad77c9d1c265bca0c63d098d" +
                "984f20684ec5897023e6c19cf06a5cc3e5f5ae7c7e45900a6485836207a4454128e9c6eb86f151606321a68a99ad4d" +
                "0e1235489d0c62ffca462e5be075656ed3bb4788238fb555cda5efe8690192fe7319472655975c5e0ea09b41b8696" +
                "df8422fe2ae259d8c50709640a135f660af6338b0a18b2e948f4dd2fb355dcaf097d75cd74337c930efba9bb51d40f" +
                "95424b091cbe4be6447ac28c3540b12ca3e35e076c37b02b5a1661f6e8d925413bf866e5f5316d76706614581963ae" +
                "622cae906d1d68f076eebfd601668e0c87419e74419f3b0deeece71786f2915b64fc1c97779a953f7cc2770936577" +
                "581e3938e6cf85f12784beea404abb446754c2bf5016a0ba7862502a587d995f08bd284ebb2f45aa07646bc6e1daef" +
                "dc5047e2fea41ecc954067ed9e23902d6a8033f68c2b2232f0a9ae7db7a8dc53ef956aab7e04c2b2ffbfb0d031ffd" +
                "36bf6e4c081a209485bdb755b4a1b677a515cebda98035534df3ca7f1c1127d15acde5c75b77f2cf218df18b8566d" +
                "75b30488179b18756839b2c88f4d3dd84ea1044abfe791c32ba2dc5d0d7569a9600c58286b3c2c2c475b9bb2138e47" +
                "895139452e92a941daca3f2bcaa8c3dbd7dffa0252b3ae7421597e385ecf9a036dcdd86e6a1d817f2fc41b1c73caf" +
                "df4805f28de37e32df6e8d0b0acc4e57c976c536023961992d7201df5d47a07cd8f8a83faefa3b55443029578cb1b" +
                "a9dfdddc2acf9172d141de04505c137098f9cf19e4baf82447266861182411a31a1f02a1b418b5330fa225803fd3f" +
                "56e868dfb17da635f98cc18033856da8a3eefae0f6541d0517ba0f28169521e5d8764d69eda3fe76a7de8d9b45e92" +
                "c5f16978b67e44ff4edcdca2a4cdef1286e2ea9fbbfb9f6c0a432b45867803ef97384272e1f71a9c11d434a6b7b74" +
                "48bc64aacf483eb8b496560142ccfdfad1faccba50bd5ff12fda1704622dcbcaf8dc7b07c963b1d429d4da7f5b33" +
                "903b45ff4020187581edfdb67132f85bf08b133553d708d29c3721fd867b5e170eb70e65c9cf5b344944df2e879760" +
                "c7037e39383c24028e6648f59f092b5bacaceb5d282f31a3969dbeba350cb907557553e26a7f2fd204487b7a7c2099" +
                "db4a6ff3e4621974985165227c087c866ab18632b425c94c8a9a297f1099923c2c6003181c0c788b3f63584e9870d" +
                "eef0b4a95260af3d411fa127c9ff56ce9f3d1195485e15cfba7623a21955d7f0d2e5dc2622c4cda3a23e4c9500ba9b" +
                "46285a7cab36a56c01e4aa371be55c99dc14cd4d386cb65bbf82617b4c83f0574b99da242c6dc814726bdcad4708db" +
                "d76b9b42dc44325951b40cfedbc68e744b3f95ecabf0383314764aecab1416e449e00ec8f59552f19e2d689dd3c5d2" +
                "2565f20ed63ab97d95c5694b75858fdbd20920d20629c638eec08a76feb079d2edc1798f3e34b3d7648571dff2f8a9" +
                "e653d173f196a4bb1c45987312c77b2b41b2448677953aff1671763f2767d7ac7e7a2a605934d751a6c9dbf9351e" +
                "ea1975cd1104504ca02656f7a8b9e3e886768bf4466b7b3b2f64ef89f8946e88b8113314aed94ae9058abb2b8e5fc" +
                "211d4363b34ca026e6d9ff052ae9e21feb7b23964041bb763f6f93d5b0b0f5499a662eda86adeb0b98d2c1b8db80" +
                "8dedc3001e5af49abc66df3ecb1134277480097137d0f1641d7f7442c78c38079626f7a7b3f0f04e6094681ec8eaa" +
                "f4e1b2a4d5ad2f5909355f588db016e831eecaef81e05f465323113da5a5f5056481a3923c77ecf237a4a18404de9" +
                "1a9eaace74a2dcf2d66fa1437df048784c9de024ef674e50113f18b100c48f6f4e6a2ba88ef3f8d80c6aa9d08eeeb3" +
                "e198c1c483c7cfee4b275cc2af6886c85dc551b2e7063a2fe48909a37febdbd5effb4665f2b19992356f92af45121a" +
                "d6ea3889ab34c4d2b56dee7147090cb7739b42291c11944868a50ea45f671c8fca418e5c396f02eb3e8082f0ebeda" +
                "069f8518294211aa9e6efea55693e8646a7f214d12de55f0e5fb889f113d99c2211c18e6258e17789b5773479dbd7b" +
                "905517c375f23d60951444707a5292fadb7da6f53770938decac8580c8584984106e9bc13d5c307e5a78af4e8093bc" +
                "abfb5e8a2eee50023ff0ece77cad282f2fd14b505b339ab3dbc87eada2a6e6cde041ec5d5ddb32fcab2d94e1335fab" +
                "55a5d14973b4d73201103b283115a1438d76265dd7a07c6cbb30cf6329d57503fdd92dbdd4efd3fa5a60c57b7d397" +
                "80e6f846";
        val json = "[" +
                "  {" +
                "    \"ipIdentity\": 0," +
                "    \"ipDescription\": {" +
                "      \"name\": \"Concordium testnet IP\"," +
                "      \"url\": \"\"," +
                "      \"description\": \"Concordium testnet identity provider\"" +
                "    }," +
                "    \"ipVerifyKey\": \"" + psPublicKey + "\"," +
                "    \"ipCdiVerifyKey\": \"2e1cff3988174c379432c1fad7ccfc385c897c4477c06617262cec7193226eca\"" +
                "  }" +
                "]";
        val jsonVal = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();
        val ips = IdentityProviderInfo.fromJsonArray(jsonVal);

        Assert.assertTrue(ips.isPresent());
        Assert.assertEquals(Arrays.stream(ips.get()).count(), 1);
        Assert.assertEquals(Arrays.stream(ips.get()).findFirst().get(), new IdentityProviderInfo(
                0,
                new Description("Concordium testnet IP", "", "Concordium testnet identity provider"),
                ED25519PublicKey.from("2e1cff3988174c379432c1fad7ccfc385c897c4477c06617262cec7193226eca"),
                PSPublicKey.from(psPublicKey)
        ));
    }

    @Test
    public void testShouldHandleNullJson() {
        val json = "null";
        val jsonVal = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();
        val ips = IdentityProviderInfo.fromJsonArray(jsonVal);

        Assert.assertFalse(ips.isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShouldHandleInvalidJson() {
        val json = "{";
        val jsonVal = ConcordiumP2PRpc.JsonResponse.newBuilder().setValue(json).build();
        val ips = IdentityProviderInfo.fromJsonArray(jsonVal);
    }
}
