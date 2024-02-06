package com.concordium.sdk.transactions;

import com.concordium.sdk.cis2.Cis2Transfer;
import com.concordium.sdk.cis2.SerializationUtils;
import com.concordium.sdk.cis2.events.Cis2Event;
import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.types.*;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.util.Arrays;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class TransactionSerializationTest {

    @Test
    public void testSerializeAndDeserializeSimpleTransfer() {
        ByteBuffer serializedBi = ByteBuffer.wrap(bi.getVersionedBytes());
        BlockItem deserializedBlockItem = BlockItem.fromVersionedBytes(serializedBi);
        assertEquals("Block items should match", bi, deserializedBlockItem);
        if(deserializedBlockItem.getBlockItemType() == BlockItemType.ACCOUNT_TRANSACTION) {
            AccountTransaction accountTransaction = (AccountTransaction)deserializedBlockItem;
            TransactionType transactionType = accountTransaction.getPayload().getTransactionType();
            if(transactionType == TransactionType.SIMPLE_TRANSFER) {
                Transfer payload = (Transfer) accountTransaction.getPayload();
                assertEquals(payload.getPayload().getAmount(), CCDAmount.fromMicro(17));
            }else {
                throw new RuntimeException("Should be a simple transfer");
            }
        } else {
            throw new RuntimeException("Should be an account transaction");
        }
    }

    @Test
    @SneakyThrows
    public void testDeserializeSimpleTransfer() {
        BlockItem blockItem = BlockItem.fromVersionedBytes(ByteBuffer.wrap(Hex.decodeHex(BI_TRANSFER)));
        assertEquals(BlockItemType.ACCOUNT_TRANSACTION, blockItem.getBlockItemType());
        Payload payload = ((AccountTransaction) blockItem).getPayload();
        // check signature
        TransactionSignature signature = ((AccountTransaction) blockItem).getSignature();
        assertEquals(1, signature.getSignatures().size());
        assertEquals(Signature.from(Hex.decodeHex("afb0dbb92c8fdc1eb16959c3eb8b6e9a69e5634c0ff4da2966b79dbdca0f69576c5b0f401175bc18ce7d296b926fddcd48a3bbcb74fde13d65800712992f0502")), signature.getSignatures().get(Index.from(0)).getSignatures().get(Index.from(0)));
        // check header
        TransactionHeader header = ((AccountTransaction) blockItem).getHeader();
        assertEquals(AccountAddress.from("3LFSxgiU4d7i1irUx1pNrQFFC74RxHBRaNztKWKDVJ1FJN8UP1"), header.getSender());
        assertEquals(Nonce.from(1), header.getAccountNonce());
        assertEquals(UInt64.from(1690898672), header.getExpiry());
        // check payload
        TransactionType transactionType = payload.getTransactionType();
        assertEquals(TransactionType.SIMPLE_TRANSFER, transactionType);
        Transfer simpleTransfer = (Transfer) payload;
        assertEquals(CCDAmount.fromMicro(0), simpleTransfer.getPayload().getAmount());
        assertEquals(AccountAddress.from("2xM9xfrWisXeg4nBSyL4XzpGoZsx3L9otPdotcvp7euj3SB4y2"), simpleTransfer.getPayload().getReceiver());
    }

    @Test
    @SneakyThrows
    public void testDeserializeTransferWithMemo() {
        BlockItem blockItem = BlockItem.fromVersionedBytes(ByteBuffer.wrap(Hex.decodeHex(BI_TRANSFER_WITH_MEMO)));
        assertEquals(BlockItemType.ACCOUNT_TRANSACTION, blockItem.getBlockItemType());
        Payload payload = ((AccountTransaction) blockItem).getPayload();
        // check signature
        TransactionSignature signature = ((AccountTransaction) blockItem).getSignature();
        assertEquals(1, signature.getSignatures().size());
        assertEquals(Signature.from(Hex.decodeHex("52545c8418bf3228469cde4aa33ed8bb646aaffc6595c7bfcc1c0a4f353c1b1b874f5bd54a4e953c4a38e6529ae2e1b9a44597505d0676b8f7d15741b62fba00")), signature.getSignatures().get(Index.from(0)).getSignatures().get(Index.from(0)));
        // check header
        TransactionHeader header = ((AccountTransaction) blockItem).getHeader();
        assertEquals(AccountAddress.from("4aM9746fwvhvh567MWWvmAgEtYmN9TwfhdEbWLRY8FzomGrtps"), header.getSender());
        assertEquals(Nonce.from(5), header.getAccountNonce());
        assertEquals(UInt64.from(1690969995), header.getExpiry());
        // check payload
        TransactionType transactionType = payload.getTransactionType();
        assertEquals(TransactionType.TRANSFER_WITH_MEMO, transactionType);
        TransferWithMemo memoTransfer = (TransferWithMemo) payload;
        assertEquals(CCDAmount.from(200), memoTransfer.getPayload().getAmount());
        assertEquals(AccountAddress.from("3NxJgE2G3pfbVk1N1FuQdBqxsketNdYAudm6nNnNtAw2eDfvTi"), memoTransfer.getPayload().getReceiver());
        assertEquals(Memo.from(new byte[]{100, 116, 101, 115, 116}), memoTransfer.getPayload().getMemo());
    }

    @SneakyThrows
    @Test
    public void testDeserializeContractUpdate() {
        BlockItem blockItem = BlockItem.fromVersionedBytes(ByteBuffer.wrap(Hex.decodeHex(BI_CONTRACT_UPDATE)));
        assertEquals(BlockItemType.ACCOUNT_TRANSACTION, blockItem.getBlockItemType());
        Payload payload = ((AccountTransaction) blockItem).getPayload();
        // check signature
        TransactionSignature signature = ((AccountTransaction) blockItem).getSignature();
        assertEquals(1, signature.getSignatures().size());
        assertEquals(Signature.from(Hex.decodeHex("a3e911353ae8558d42b149683c5f96c5bd2fe7a7b03f6ad879f9fe7c12f95b6bd51fba0fd305d680a37a0555c9d281e3ba5c5091f6584e784f7bb1c73855b608")), signature.getSignatures().get(Index.from(0)).getSignatures().get(Index.from(0)));
        // check header
        TransactionHeader header = ((AccountTransaction) blockItem).getHeader();
        assertEquals(AccountAddress.from("49NGYqmPtbuCkXSQt7298mL6Xp52UpSR4U2jVzJjKW9P3b3whw"), header.getSender());
        assertEquals(Nonce.from(21), header.getAccountNonce());
        assertEquals(UInt64.from(1706724577), header.getExpiry());
        // check payload
        TransactionType transactionType = payload.getTransactionType();
        assertEquals(TransactionType.UPDATE_SMART_CONTRACT_INSTANCE, transactionType);
        UpdateContract updateContract = (UpdateContract) payload;
        assertEquals(CCDAmount.from(0), updateContract.getAmount());
        assertEquals(ContractAddress.from(9390, 0), updateContract.getContractAddress());
        assertEquals(ReceiveName.from("euroe_stablecoin", "transfer"), updateContract.getReceiveName());
        Cis2Transfer expectedParameters = new Cis2Transfer(new byte[0], 11078313, AccountAddress.from("49NGYqmPtbuCkXSQt7298mL6Xp52UpSR4U2jVzJjKW9P3b3whw"), AccountAddress.from("4sGtbuGKgakv5pKSMsy3CEQbW3sn2PbTzTVLZLA6zxX5bB3C5a"), null);
        byte[] expectedParams = updateContract.getParam().getBytes();
        byte[] cis2TransferParams = SerializationUtils.serializeTransfers(Lists.newArrayList(expectedParameters)).getBytes();
        assertArrayEquals(expectedParams, Arrays.concatenate(UInt16.from(cis2TransferParams.length).getBytes(), cis2TransferParams));
    }


    // a hex encoded normal transfer
    private static final String BI_TRANSFER = "0000010001000040afb0dbb92c8fdc1eb16959c3eb8b6e9a69e5634c0ff4da2966b79dbdca0f69576c5b0f401175bc18ce7d296b926fddcd48a3bbcb74fde13d65800712992f05023319809bed082478e4762c135db732d058b66ff3ac374ece15f484ad3ac95039000000000000000100000000000001f5000000290000000064c910f003015e8d5ce9525d4630f7fbdd58dacebefb64b964b241da3f921521d937b49ba80000000000000000";

    // a hex encoded transfer with memo
    private static final String BI_TRANSFER_WITH_MEMO = "000001000100004052545c8418bf3228469cde4aa33ed8bb646aaffc6595c7bfcc1c0a4f353c1b1b874f5bd54a4e953c4a38e6529ae2e1b9a44597505d0676b8f7d15741b62fba00d6cea90316c56d994abd205876e423c6f6c9488e2d7c4f0b044539de501f1174000000000000000500000000000001fc000000300000000064ca278b16393d88b218e44301cceb22bd64184edba56996498523710ebb43b4cf430db89700056474657374000000000bebc200";

    private static final String BI_CONTRACT_UPDATE = "0000010001000040a3e911353ae8558d42b149683c5f96c5bd2fe7a7b03f6ad879f9fe7c12f95b6bd51fba0fd305d680a37a0555c9d281e3ba5c5091f6584e784f7bb1c73855b6089e15fc57bbe167411d4d9c0686e31e8e937d751625972f7c566de4a97f650dc500000000000000150000000000002831000000810000000065ba8ce102000000000000000000000000000024ae000000000000000000196575726f655f737461626c65636f696e2e7472616e73666572004b010000a995a405009e15fc57bbe167411d4d9c0686e31e8e937d751625972f7c566de4a97f650dc500fd3dd07c83e42461554cf0dd90d73c1ff04531fc2b9c90b9762df8793319e48d0000";

    private static final BlockItem bi = Transfer.createNew(
                    AccountAddress.from("3hYXYEPuGyhFcVRhSk2cVgKBhzVcAryjPskYk4SecpwGnoHhuM"),
                    CCDAmount.fromMicro(17))
            .withHeader(TransactionHeader
                    .builder()
                    .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                    .accountNonce(Nonce.from(78910))
                    .expiry(UInt64.from(123456))
                    .build())
            .signWith(
                    TransactionSigner.from(
                            SignerEntry.from(Index.from(0), Index.from(0),
                                    ED25519SecretKey.from("7100071c835a0a35e86dccba7ee9d10b89e36d1e596771cdc8ee36a17f7abbf2")),
                            SignerEntry.from(Index.from(0), Index.from(1),
                                    ED25519SecretKey.from("cd20ea0127cddf77cf2c20a18ec4516a99528a72e642ac7deb92131a9d108ae9"))
                    )
            ).toBlockItem();


}
