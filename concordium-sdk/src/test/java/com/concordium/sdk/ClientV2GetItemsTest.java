package com.concordium.sdk;

import com.concordium.grpc.v2.AccountTransaction;
import com.concordium.grpc.v2.BlockItem;
import com.concordium.grpc.v2.InitContractPayload;
import com.concordium.grpc.v2.InitName;
import com.concordium.grpc.v2.Memo;
import com.concordium.grpc.v2.Parameter;
import com.concordium.grpc.v2.ReceiveName;
import com.concordium.grpc.v2.Signature;
import com.concordium.grpc.v2.TransferPayload;
import com.concordium.grpc.v2.TransferWithMemoPayload;
import com.concordium.grpc.v2.UpdateContractPayload;
import com.concordium.grpc.v2.*;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.transactions.*;
import com.concordium.sdk.transactions.smartcontracts.WasmModule;
import com.concordium.sdk.transactions.smartcontracts.WasmModuleVersion;
import com.concordium.sdk.types.Nonce;
import com.concordium.sdk.types.UInt32;
import com.concordium.sdk.types.UInt64;
import com.google.common.collect.ImmutableList;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcCleanupRule;
import lombok.val;
import lombok.var;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static com.concordium.sdk.ClientV2MapperExtensions.to;
import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ClientV2GetItemsTest {
    private static final com.concordium.sdk.types.AccountAddress ACCOUNT_ADDRESS_1
            = com.concordium.sdk.types.AccountAddress.from("37UHs4b9VH3F366cdmrA4poBURzzARJLWxdXZ18zoa9pnfhhDf");
    private static final long ACCOUNT_TRANSACTION_ENERGY = 300;
    private static final long ACCOUNT_TRANSACTION_EXPIRY = 8989;
    private static final long ACCOUNT_TRANSACTION_SEQ_NO = 7;
    private static final int ACCOUNT_TRANSACTION_PAYLOAD_SIZE = 11;
    private static final byte[] ACCOUNT_TRANSACTION_SIGNATURE_BYTES = new byte[]{1, 1, 1};
    private static final byte[] MODULE_V0_BYTES = new byte[]{0, 1, 1};
    private static final long CREDENTIAL_DEPLOYMENT_EXPIRY_TIMESTAMP = 100000;
    private static final byte[] CREDENTIAL_DEPLOYMENT_PAYLOAD = new byte[]{10, 9, 8};
    private static final long UPDATE_INSTRUCTION_EFFECTIVE_TIME = 100001;
    private static final long UPDATE_INSTRUCTION_SEQ_NUM = 10;
    private static final long UPDATE_INSTRUCTION_TIMEOUT = 88888;
    private static final byte[] UPDATE_INSTRUCTION_SIG_0 = new byte[]{99, 99, 99};
    private static final byte[] UPDATE_INSTRUCTION_PAYLOAD = new byte[]{45, 56, 67};

    private static final AccountTransactionHeader ACCOUNT_TRANSACTION_HEADER = AccountTransactionHeader.newBuilder()
            .setEnergyAmount(Energy.newBuilder().setValue(ACCOUNT_TRANSACTION_ENERGY).build())
            .setExpiry(TransactionTime.newBuilder().setValue(ACCOUNT_TRANSACTION_EXPIRY).build())
            .setSender(AccountAddress.newBuilder().setValue(ByteString.copyFrom(ACCOUNT_ADDRESS_1.getBytes())).build())
            .setSequenceNumber(SequenceNumber.newBuilder().setValue(ACCOUNT_TRANSACTION_SEQ_NO).build())
            .build();
    private static final AccountTransactionSignature ACCOUNT_TRANSACTION_SIGNATURE
            = AccountTransactionSignature.newBuilder()
            .putSignatures(0, AccountSignatureMap.newBuilder()
                    .putSignatures(0, Signature.newBuilder()
                            .setValue(ByteString.copyFrom(ACCOUNT_TRANSACTION_SIGNATURE_BYTES))
                            .build())
                    .build())
            .build();
    private static final AccountTransactionPayload DEPLOY_MODULE_PAYLOAD = AccountTransactionPayload.newBuilder()
            .setDeployModule(VersionedModuleSource.newBuilder()
                    .setV0(VersionedModuleSource.ModuleSourceV0.newBuilder()
                            .setValue(ByteString.copyFrom(MODULE_V0_BYTES))
                            .build())
                    .build())
            .build();
    private static final AccountTransaction ACCOUNT_TRANSACTION_DEPLOY_MODULE_V0 = AccountTransaction.newBuilder()
            .setHeader(ACCOUNT_TRANSACTION_HEADER)
            .setSignature(ACCOUNT_TRANSACTION_SIGNATURE)
            .setPayload(DEPLOY_MODULE_PAYLOAD)
            .build();
    private static final CredentialDeployment CREDENTIAL_DEPLOYMENT = CredentialDeployment.newBuilder()
            .setMessageExpiry(TransactionTime.newBuilder().setValue(CREDENTIAL_DEPLOYMENT_EXPIRY_TIMESTAMP).build())
            .setRawPayload(ByteString.copyFrom(CREDENTIAL_DEPLOYMENT_PAYLOAD))
            .build();
    private static final UpdateInstruction UPDATE_INSTRUCTION = UpdateInstruction.newBuilder()
            .setHeader(UpdateInstructionHeader.newBuilder()
                    .setEffectiveTime(TransactionTime.newBuilder().setValue(UPDATE_INSTRUCTION_EFFECTIVE_TIME).build())
                    .setSequenceNumber(UpdateSequenceNumber.newBuilder().setValue(UPDATE_INSTRUCTION_SEQ_NUM).build())
                    .setTimeout(TransactionTime.newBuilder().setValue(UPDATE_INSTRUCTION_TIMEOUT).build())
                    .build())
            .setSignatures(SignatureMap.newBuilder().putSignatures(0, Signature.newBuilder()
                    .setValue(ByteString.copyFrom(UPDATE_INSTRUCTION_SIG_0))
                    .build()).build())
            .setPayload(UpdateInstructionPayload.newBuilder()
                    .setRawPayload(ByteString.copyFrom(UPDATE_INSTRUCTION_PAYLOAD))
                    .build())
            .build();
    private static final QueriesGrpc.QueriesImplBase serviceImpl = mock(QueriesGrpc.QueriesImplBase.class, delegatesTo(
            new QueriesGrpc.QueriesImplBase() {
                @Override
                public void getBlockItems(BlockHashInput request, StreamObserver<BlockItem> responseObserver) {
                    responseObserver.onNext(BlockItem.newBuilder()
                            .setAccountTransaction(ACCOUNT_TRANSACTION_DEPLOY_MODULE_V0)
                            .build());
                    responseObserver.onNext(BlockItem.newBuilder()
                            .setCredentialDeployment(CREDENTIAL_DEPLOYMENT)
                            .build());
                    responseObserver.onNext(BlockItem.newBuilder()
                            .setUpdateInstruction(UPDATE_INSTRUCTION)
                            .build());
                    responseObserver.onCompleted();
                }
            }
    ));
    private static final TransactionHeader ACCOUNT_TRANSACTION_HEADER_EXPECTED = TransactionHeader.from(
            ACCOUNT_ADDRESS_1,
            Nonce.from(ACCOUNT_TRANSACTION_SEQ_NO),
            UInt64.from(ACCOUNT_TRANSACTION_EXPIRY),
            UInt64.from(ACCOUNT_TRANSACTION_ENERGY),
            UInt32.from(ACCOUNT_TRANSACTION_PAYLOAD_SIZE));
    private static final TransactionSignature ACCOUNT_TRANSACTION_SIGNATURE_EXPECTED = TransactionSignature.builder()
            .signature(Index.from(0), TransactionSignatureAccountSignatureMap.builder()
                    .signature(
                            Index.from(0),
                            com.concordium.sdk.transactions.Signature.from(ACCOUNT_TRANSACTION_SIGNATURE_BYTES))
                    .build())
            .build();
    private static final DeployModuleTransaction ACCOUNT_TRANSACTION_DEPLOY_MODULE_V0_EXPECTED = DeployModuleTransaction
            .builderBlockItem()
            .header(ACCOUNT_TRANSACTION_HEADER_EXPECTED)
            .signature(ACCOUNT_TRANSACTION_SIGNATURE_EXPECTED)
            .payload(WasmModule.from(MODULE_V0_BYTES, WasmModuleVersion.V0))
            .build();
    private static final CredentialDeploymentTransaction CREDENTIAL_DEPLOYMENT_EXPECTED = CredentialDeploymentTransaction
            .builderBlockItem()
            .expiry(UInt64.from(CREDENTIAL_DEPLOYMENT_EXPIRY_TIMESTAMP))
            .payloadBytes(CREDENTIAL_DEPLOYMENT_PAYLOAD)
            .build();
    private static final UpdateInstructionTransaction UPDATE_INSTRUCTION_EXPECTED = UpdateInstructionTransaction
            .builderBlockItem()
            .header(UpdateInstructionTransactionHeader.builder()
                    .effectiveTime(UInt64.from(UPDATE_INSTRUCTION_EFFECTIVE_TIME))
                    .sequenceNumber(UInt64.from(UPDATE_INSTRUCTION_SEQ_NUM))
                    .timeout(UInt64.from(UPDATE_INSTRUCTION_TIMEOUT))
                    .build())
            .signature(UpdateInstructionTransactionSignature.builder()
                    .signature(
                            Index.from(0),
                            com.concordium.sdk.transactions.Signature.from(UPDATE_INSTRUCTION_SIG_0))
                    .build())
            .payload(UpdateInstructionTransactionPayload.from(UPDATE_INSTRUCTION_PAYLOAD))
            .build();

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
    public void getBlockItems() {
        var res = client.getBlockItems(BlockQuery.BEST);
        val resList = ImmutableList.copyOf(res);

        verify(serviceImpl).getBlockItems(any(BlockHashInput.class), any(StreamObserver.class));
        assertEquals(ACCOUNT_TRANSACTION_DEPLOY_MODULE_V0_EXPECTED, resList.get(0));
        assertEquals(CREDENTIAL_DEPLOYMENT_EXPECTED, resList.get(1));
        assertEquals(UPDATE_INSTRUCTION_EXPECTED, resList.get(2));
    }

    @Test
    public void shouldMapInitContractTransaction() {
        final long amount = 10L;
        final byte[] moduleRef = new byte[]{7, 7, 7};
        final byte[] parameter = new byte[]{8, 8, 8};
        final String initName = "init_name";

        val mapped = to(ACCOUNT_TRANSACTION_DEPLOY_MODULE_V0.toBuilder()
                .setPayload(AccountTransactionPayload.newBuilder()
                        .setInitContract(InitContractPayload.newBuilder()
                                .setAmount(Amount.newBuilder().setValue(amount).build())
                                .setInitName(InitName.newBuilder().setValue(initName).build())
                                .setModuleRef(ModuleRef
                                        .newBuilder()
                                        .setValue(ByteString.copyFrom(moduleRef))
                                        .build())
                                .setParameter(Parameter
                                        .newBuilder()
                                        .setValue(ByteString.copyFrom(parameter))
                                        .build())
                                .build())
                        .build())
                .build());
        val expected = InitContractTransaction.builderBlockItem()
                .header(ACCOUNT_TRANSACTION_HEADER_EXPECTED.toBuilder().payloadSize(UInt32.from(27)).build())
                .signature(ACCOUNT_TRANSACTION_SIGNATURE_EXPECTED)
                .payload(com.concordium.sdk.transactions.InitContractPayload.from(amount,
                        moduleRef,
                        initName,
                        parameter))
                .build();

        assertEquals(expected, mapped);
    }

    @Test
    public void shouldMapUpdateContractTransaction() {
        final long amount = 10L;
        final byte[] parameter = new byte[]{8, 8, 8};

        val mapped = to(ACCOUNT_TRANSACTION_DEPLOY_MODULE_V0.toBuilder()
                .setPayload(AccountTransactionPayload.newBuilder()
                        .setUpdateContract(UpdateContractPayload.newBuilder()
                                .setAmount(Amount.newBuilder().setValue(amount).build())
                                .setAddress(ContractAddress.newBuilder().setIndex(1).setSubindex(0).build())
                                .setReceiveName(ReceiveName.newBuilder()
                                        .setValue("contract.method")
                                        .build())
                                .setParameter(Parameter
                                        .newBuilder()
                                        .setValue(ByteString.copyFrom(parameter))
                                        .build())
                                .build())
                        .build())
                .build());
        val expected = UpdateContractTransaction.builderBlockItem()
                .header(ACCOUNT_TRANSACTION_HEADER_EXPECTED.toBuilder().payloadSize(UInt32.from(46)).build())
                .signature(ACCOUNT_TRANSACTION_SIGNATURE_EXPECTED)
                .payload(com.concordium.sdk.transactions.UpdateContractPayload.from(amount,
                        com.concordium.sdk.types.ContractAddress.from(1, 0),
                        "contract",
                        "method",
                        parameter))
                .build();

        assertEquals(expected, mapped);
    }

    @Test
    public void shouldMapRegisterDataTransaction() {
        final byte[] dataBytes = new byte[]{9, 9, 9};

        val mapped = to(ACCOUNT_TRANSACTION_DEPLOY_MODULE_V0.toBuilder()
                .setPayload(AccountTransactionPayload.newBuilder()
                        .setRegisterData(RegisteredData.newBuilder().setValue(ByteString.copyFrom(dataBytes)))
                        .build())
                .build());
        val expected = RegisterDataTransaction.builderBlockItem()
                .header(ACCOUNT_TRANSACTION_HEADER_EXPECTED.toBuilder().payloadSize(UInt32.from(5)).build())
                .signature(ACCOUNT_TRANSACTION_SIGNATURE_EXPECTED)
                .payload(Data.from(dataBytes))
                .build();

        assertEquals(expected, mapped);
    }

    @Test
    public void shouldMapTransferTransaction() {
        final byte[] dataBytes = new byte[]{9, 9, 9};
        final long amount = 10L;
        final byte[] recieverAccountAddress = com.concordium.sdk.types.AccountAddress
                .from("37UHs4b9VH3F366cdmrA4poBURzzARJLWxdXZ18zoa9pnfhhDf").getBytes();

        val mapped = to(ACCOUNT_TRANSACTION_DEPLOY_MODULE_V0.toBuilder()
                .setPayload(AccountTransactionPayload.newBuilder()
                        .setTransfer(TransferPayload.newBuilder()
                                .setAmount(Amount.newBuilder().setValue(amount).build())
                                .setReceiver(AccountAddress.newBuilder()
                                        .setValue(ByteString.copyFrom(recieverAccountAddress))
                                        .build())
                                .build())
                        .build())
                .build());
        val expected = TransferTransaction.builderBlockItem()
                .header(ACCOUNT_TRANSACTION_HEADER_EXPECTED.toBuilder().payloadSize(UInt32.from(40)).build())
                .signature(ACCOUNT_TRANSACTION_SIGNATURE_EXPECTED)
                .payload(com.concordium.sdk.transactions.TransferPayload.from(
                        com.concordium.sdk.types.AccountAddress.from(recieverAccountAddress),
                        CCDAmount.fromMicro(amount)))
                .build();

        assertEquals(expected, mapped);
    }

    @Test
    public void shouldMapTransferWithMemoTransaction() {
        final byte[] dataBytes = new byte[]{9, 9, 9};
        final long amount = 10L;
        final byte[] recieverAccountAddress = com.concordium.sdk.types.AccountAddress
                .from("37UHs4b9VH3F366cdmrA4poBURzzARJLWxdXZ18zoa9pnfhhDf").getBytes();
        final byte[] memoBytes = new byte[]{10, 10, 10};

        val mapped = to(ACCOUNT_TRANSACTION_DEPLOY_MODULE_V0.toBuilder()
                .setPayload(AccountTransactionPayload.newBuilder()
                        .setTransferWithMemo(TransferWithMemoPayload.newBuilder()
                                .setAmount(Amount.newBuilder().setValue(amount).build())
                                .setReceiver(AccountAddress.newBuilder()
                                        .setValue(ByteString.copyFrom(recieverAccountAddress))
                                        .build())
                                .setMemo(Memo.newBuilder().setValue(ByteString.copyFrom(memoBytes)).build())
                                .build())
                        .build())
                .build());
        val expected = TransferWithMemoTransaction.builderBlockItem()
                .header(ACCOUNT_TRANSACTION_HEADER_EXPECTED.toBuilder().payloadSize(UInt32.from(45)).build())
                .signature(ACCOUNT_TRANSACTION_SIGNATURE_EXPECTED)
                .payload(com.concordium.sdk.transactions.TransferWithMemoPayload.from(
                        com.concordium.sdk.types.AccountAddress.from(recieverAccountAddress),
                        CCDAmount.fromMicro(amount),
                        com.concordium.sdk.transactions.Memo.from(memoBytes)))
                .build();

        assertEquals(expected, mapped);
    }

    @Test
    public void shouldMapRawTransaction() {
        final byte[] payloadBytes = new byte[]{11, 11, 11};

        val mapped = to(ACCOUNT_TRANSACTION_DEPLOY_MODULE_V0.toBuilder()
                .setPayload(AccountTransactionPayload.newBuilder()
                        .setRawPayload(ByteString.copyFrom(payloadBytes))
                        .build())
                .build());
        val expected = com.concordium.sdk.transactions.AccountTransaction
                .builderAccountTransactionBlockItem()
                .header(ACCOUNT_TRANSACTION_HEADER_EXPECTED.toBuilder().payloadSize(UInt32.from(3)).build())
                .signature(ACCOUNT_TRANSACTION_SIGNATURE_EXPECTED)
                .payloadBytes(payloadBytes)
                .build();

        assertEquals(expected, mapped);
    }

    @Test
    public void shouldMapPayloadNotSetTransaction() {
        val mapped = to(ACCOUNT_TRANSACTION_DEPLOY_MODULE_V0.toBuilder()
                .setPayload(AccountTransactionPayload.newBuilder().build())
                .build());
        val expected = com.concordium.sdk.transactions.AccountTransaction
                .builderAccountTransactionBlockItem()
                .header(ACCOUNT_TRANSACTION_HEADER_EXPECTED.toBuilder().payloadSize(UInt32.from(0)).build())
                .signature(ACCOUNT_TRANSACTION_SIGNATURE_EXPECTED)
                .payloadBytes(new byte[0])
                .build();

        assertEquals(expected, mapped);
    }

    @Test
    public void shouldMapModuleV1() {
        final byte[] moduleBytes = new byte[]{1, 1, 1};

        var actual = to(VersionedModuleSource.newBuilder()
                .setV1(VersionedModuleSource.ModuleSourceV1.newBuilder()
                        .setValue(ByteString.copyFrom(moduleBytes))
                        .build())
                .build());
        var expected = WasmModule.from(moduleBytes, WasmModuleVersion.V1);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldMapUpdateInstructionPayloadNotSet() {
        var res = to(UpdateInstructionPayload.newBuilder().build());
        assertEquals(UpdateInstructionTransactionPayload.from(new byte[0]), res);
    }

    @Test
    public void shouldMapCredentialsDeployment() {
        final long time = 10;

        var res = to(CredentialDeployment.newBuilder()
                .setMessageExpiry(TransactionTime.newBuilder().setValue(time).build())
                .build());
        assertEquals(CredentialDeploymentTransaction.from(UInt64.from(time), new byte[0]), res);
    }
}
