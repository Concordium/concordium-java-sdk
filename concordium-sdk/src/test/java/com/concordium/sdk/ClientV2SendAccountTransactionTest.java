package com.concordium.sdk;

import com.concordium.grpc.v2.AccountTransaction;
import com.concordium.grpc.v2.Signature;
import com.concordium.grpc.v2.*;
import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.transactions.*;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.Nonce;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcCleanupRule;
import lombok.var;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ClientV2SendAccountTransactionTest {
    private static final long ENERGY = 501L;
    private static final long SEQUENCE_NUMBER = 60L;
    private static final long EXPIRY = 1000000L;
    private static final byte[] RAW_PAYLOAD = new byte[]{
            3, 74, -128, 120, 81, -55, 58, -115, 27, -8, 74, -74, 29, -37, 31, -113, 115, -36, -123, -51, -58, -33, 20,
            80, 40, 15, 28, -101, 43, -10, -109, 105, -77, 0, 0, 0, 0, 0, 15, 66, 64};
    private static final byte[] SIGNATURE_BYTES = new byte[]{
            -88, 8, -79, -3, -5, 6, 102, 90, -65, -14, 107, -24, 98, 45, 66, -84, -119, -89, 64, -98, 89, 40, -114, 58,
            -66, 33, -61, -79, 90, -124, -103, 33, -67, 117, 111, 82, 7, -103, 57, -58, -62, -122, -120, -3, 48, -77,
            -76, -4, 59, -98, -103, 36, 123, 43, -89, -37, -4, 44, -71, 98, -17, 99, -124, 0};
    private static final byte[] TRANSACTION_HASH_BYTES;

    static {
        try {
            TRANSACTION_HASH_BYTES = Hex.decodeHex("38738a239408e13fd6746d2ff8f81f4870910a82b5359668ac5fec57446cc708");
        } catch (DecoderException e) {
            throw new RuntimeException(e);
        }
    }

    private static final AccountAddress SENDER_ACCOUNT_ADDRESS
            = AccountAddress.from("3WZE6etUvVp1eyhEtTxqZrQaanTAZnZCHEmZmDyCbCwxnmQuPE");
    private static final AccountAddress RECEIVER_ACCOUNT_ADDRESS
            = AccountAddress.from("3WZE6etUvVp1eyhEtTxqZrQaanTAZnZCHEmZmDyCbCwxnmQuPE");
    private static final long AMOUNT = 1000_000L;

    private static final QueriesGrpc.QueriesImplBase serviceImpl = mock(QueriesGrpc.QueriesImplBase.class, delegatesTo(
            new QueriesGrpc.QueriesImplBase() {
                @Override
                public void sendBlockItem(
                        final SendBlockItemRequest request,
                        final StreamObserver<TransactionHash> responseObserver) {
                    responseObserver.onNext(TransactionHash.newBuilder()
                            .setValue(ByteString.copyFrom(TRANSACTION_HASH_BYTES))
                            .build());
                    responseObserver.onCompleted();
                }
            }
    ));

    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();
    private ClientV2 client;

    @Before
    public void setUp() throws Exception {
        String serverName = InProcessServerBuilder.generateName();
        grpcCleanup.register(InProcessServerBuilder
                .forName(serverName).directExecutor().addService(serviceImpl).build().start());
        ManagedChannel channel = grpcCleanup.register(
                InProcessChannelBuilder.forName(serverName).directExecutor().build());
        client = new ClientV2(10000, channel, Optional.empty());
    }

    @Test
    public void sendBlockItem() {
        TransactionSigner signer = TransactionSigner.from(
                SignerEntry.from(Index.from(0), Index.from(0),
                        ED25519SecretKey
                                .from("56f60de843790c308dac2d59a5eec9f6b1649513f827e5a13d7038accfe31784")));

        var transactionHash = client.sendTransaction(TransactionFactory.newTransfer()
                .sender(SENDER_ACCOUNT_ADDRESS)
                .receiver(RECEIVER_ACCOUNT_ADDRESS)
                .amount(CCDAmount.fromMicro(AMOUNT))
                .nonce(Nonce.from(SEQUENCE_NUMBER))
                .expiry(Expiry.from(EXPIRY))
                .signer(signer)
                .build());

        var expectedBlockItem = SendBlockItemRequest.newBuilder()
                .setAccountTransaction(AccountTransaction.newBuilder()
                        .setHeader(AccountTransactionHeader.newBuilder()
                                .setEnergyAmount(Energy.newBuilder().setValue(ENERGY).build())
                                .setSequenceNumber(SequenceNumber.newBuilder().setValue(SEQUENCE_NUMBER).build())
                                .setExpiry(TransactionTime.newBuilder().setValue(EXPIRY).build())
                                .setSender(com.concordium.grpc.v2.AccountAddress.newBuilder()
                                        .setValue(ByteString.copyFrom(SENDER_ACCOUNT_ADDRESS.getBytes()))
                                        .build())
                                .build())
                        .setPayload(AccountTransactionPayload.newBuilder()
                                .setRawPayload(ByteString.copyFrom(RAW_PAYLOAD))
                                .build())
                        .setSignature(AccountTransactionSignature.newBuilder()
                                .putSignatures(0, AccountSignatureMap.newBuilder()
                                        .putSignatures(0, Signature.newBuilder()
                                                .setValue(ByteString.copyFrom(SIGNATURE_BYTES))
                                                .build())
                                        .build())
                                .build())
                        .build())
                .build();

        verify(serviceImpl).sendBlockItem(eq(expectedBlockItem), any(StreamObserver.class));
        assertArrayEquals(TRANSACTION_HASH_BYTES, transactionHash.getBytes());
    }
}
