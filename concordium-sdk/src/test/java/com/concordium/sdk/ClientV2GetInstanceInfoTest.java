package com.concordium.sdk;

import com.concordium.grpc.v2.*;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.responses.smartcontracts.ContractVersion;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.ContractAddress;
import com.google.common.collect.Lists;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcCleanupRule;
import lombok.var;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.Mockito.*;

/**
 * Tests Mapping from GRPC response using {@link ClientV2MapperExtensions#to(InstanceInfo)}
 */
@RunWith(MockitoJUnitRunner.class)
public class ClientV2GetInstanceInfoTest {

    private static final com.concordium.sdk.types.AccountAddress ACCOUNT_ADDRESS_1
            = com.concordium.sdk.types.AccountAddress.from("37UHs4b9VH3F366cdmrA4poBURzzARJLWxdXZ18zoa9pnfhhDf");
    private static final long AMOUNT = 10L;
    private static final String METHOD_1 = "contract.method_1";
    private static final String METHOD_2 = "contract.method_2";
    private static final String NAME = "contract_init";
    private static final long INDEX = 123;
    private static final long SUBINDEX = 0;
    private static final com.concordium.sdk.responses.modulelist.ModuleRef MODULE_REF
            = com.concordium.sdk.responses.modulelist.ModuleRef.from(new byte[]{1, 2, 3});
    private static final InstanceInfo INSTANCE_INFO_GRPC = InstanceInfo.newBuilder()
            .setV0(InstanceInfo.V0.newBuilder()
                    .setAmount(Amount.newBuilder()
                            .setValue(AMOUNT)
                            .build())
                    .addAllMethods(Lists.newArrayList(
                            ReceiveName.newBuilder().setValue(METHOD_1).build(),
                            ReceiveName.newBuilder().setValue(METHOD_2).build()
                    ))
                    .setName(InitName.newBuilder().setValue(NAME).build())
                    .setOwner(AccountAddress.newBuilder()
                            .setValue(ByteString.copyFrom(ACCOUNT_ADDRESS_1.getBytes()))
                            .build())
                    .setSourceModule(ModuleRef.newBuilder()
                            .setValue(ByteString.copyFrom(MODULE_REF.getBytes()))
                            .build())
                    .build())
            .build();
    private static final QueriesGrpc.QueriesImplBase serviceImpl = mock(QueriesGrpc.QueriesImplBase.class, delegatesTo(
            new QueriesGrpc.QueriesImplBase() {
                @Override
                public void getInstanceInfo(InstanceInfoRequest request, StreamObserver<InstanceInfo> responseObserver) {
                    responseObserver.onNext(INSTANCE_INFO_GRPC);
                    responseObserver.onCompleted();
                }
            }
    ));

    private static final com.concordium.sdk.responses.intanceinfo.InstanceInfo INSTANCE_INFO_EXPECTED
            = com.concordium.sdk.responses.intanceinfo.InstanceInfo.builder()
            .name(NAME)
            .version(ContractVersion.V0)
            .sourceModule(MODULE_REF)
            .owner(ACCOUNT_ADDRESS_1)
            .amount(CCDAmount.fromMicro(AMOUNT))
            .methods(Lists.newArrayList(METHOD_1, METHOD_2))
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
    public void getInstanceInfoTest() {
        var res = client.getInstanceInfo(BlockQuery.BEST, ContractAddress.from(INDEX, SUBINDEX));

        verify(serviceImpl).getInstanceInfo(eq(InstanceInfoRequest.newBuilder()
                .setAddress(com.concordium.grpc.v2.ContractAddress.newBuilder()
                        .setIndex(INDEX)
                        .setSubindex(SUBINDEX)
                        .build())
                .setBlockHash(com.concordium.grpc.v2.BlockHashInput.newBuilder().setBest(Empty.getDefaultInstance()).build())
                .build()), any(StreamObserver.class));
        assertEquals(INSTANCE_INFO_EXPECTED, res);
    }
}
