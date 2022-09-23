package com.concordium.sdk.queries;

import com.concordium.sdk.Client;
import com.concordium.sdk.Connection;
import com.concordium.sdk.Credentials;
import com.concordium.sdk.exceptions.ClientInitializationException;

public class QueriesTest {

//    @SneakyThrows
//    @Test
//    public void getBakerPoolStatusTest() {
//        val client = getClient();
//        val ret = client.getPoolStatus(
//                Hash.from("9741d166fdc9b70a183d6c22f79e6f87c236f56c545c9b5f1114847fecc7ba39"),
//                Optional.of(BakerId.from(1)));
//
//        Assert.assertNotNull(ret);
//    }
//
//    @SneakyThrows
//    @Test
//    public void getPassiveDelegationPoolStatusTest() {
//        val client = getClient();
//        val ret = client.getPoolStatus(
//                Hash.from("9741d166fdc9b70a183d6c22f79e6f87c236f56c545c9b5f1114847fecc7ba39"),
//                Optional.empty());
//
//        Assert.assertNotNull(ret);
//    }

//    @SneakyThrows
//    @Test
//    public void getIdentityProvidersTest() {
//        val client = getClient();
//        val ret = client.getBakerList(
//                Hash.from("9741d166fdc9b70a183d6c22f79e6f87c236f56c545c9b5f1114847fecc7ba39"));
//
//        Assert.assertNotNull(ret);
//    }

//    @SneakyThrows
//    @Test
//    public void getIdentityProvidersTest() {
//        val client = getClient();
//        val ret = client.getAnonymityRevokers(
//                Hash.from("9741d166fdc9b70a183d6c22f79e6f87c236f56c545c9b5f1114847fecc7ba39"));
//
//        Assert.assertNotNull(ret);
//    }

//    @SneakyThrows
//    @Test
//    public void getIdentityProvidersTest() {
//        val client = getClient();
//        val ret = client.getIdentityProviders(
//                Hash.from("9741d166fdc9b70a183d6c22f79e6f87c236f56c545c9b5f1114847fecc7ba39"));
//
//        Assert.assertNotNull(ret);
//    }

//    @SneakyThrows
//    @Test
//    public void getModuleList() {
//        val client = getClient();
//        val ret = client.getModuleSource(
//                ModuleRef.from("00804f9a09361e0d372fe045d4deee7d57251fb5992305eeb030a88d65d85080"),
//                Hash.from("9741d166fdc9b70a183d6c22f79e6f87c236f56c545c9b5f1114847fecc7ba39"));
//
//        Assert.assertNotNull(ret);
//    }

//    @SneakyThrows
//    @Test
//    public void getModuleList() {
//        val client = getClient();
//        val ret = client.getModuleList(
//                Hash.from("9741d166fdc9b70a183d6c22f79e6f87c236f56c545c9b5f1114847fecc7ba39"));
//
//        Assert.assertNotNull(ret);
//    }

//    @SneakyThrows
//    @Test
//    public void getIdentityProvidersTest() {
//        val client = getClient();
//        val ret = client.getBirkParameters(
//                Hash.from("9741d166fdc9b70a183d6c22f79e6f87c236f56c545c9b5f1114847fecc7ba39"));
//
//        Assert.assertNotNull(ret);
//    }

//    @SneakyThrows
//    @Test
//    public void getIdentityProvidersTest() {
//        val client = getClient();
//        val ret = client.getIdentityProviders(
//                Hash.from("9741d166fdc9b70a183d6c22f79e6f87c236f56c545c9b5f1114847fecc7ba39"));
//
//        Assert.assertNotNull(ret);
//    }

//    @SneakyThrows
//    @Test
//    public void getBakingRewardsTest() {
//        val client = getClient();
//        val ret = client.getRewardStatus(
//                Hash.from("9741d166fdc9b70a183d6c22f79e6f87c236f56c545c9b5f1114847fecc7ba39"));
//
//        Assert.assertNotNull(ret);
//    }

//    @SneakyThrows
//    @Test
//    public void getInstanceInfoTest() {
//        val client = getClient();
//        val ret = client.getInstanceInfo(
//                new ContractAddress(0, 0),
//                Hash.from("9741d166fdc9b70a183d6c22f79e6f87c236f56c545c9b5f1114847fecc7ba39"));
//
//        Assert.assertNotNull(ret);
//    }

//    @SneakyThrows
//    @Test
//    public void getInstancesTest() {
//        val client = getClient();
//        final ImmutableList<ContractAddress> ret = client
//                .getInstances(Hash.from("9741d166fdc9b70a183d6c22f79e6f87c236f56c545c9b5f1114847fecc7ba39"));
//
//        Assert.assertNotNull(ret);
//    }

//    @SneakyThrows
//    @Test
//    public void getAccountListTest() {
//        val client = getClient();
//        final ImmutableList<AccountAddress> ret = client
//                .getAccountList(Hash.from("9741d166fdc9b70a183d6c22f79e6f87c236f56c545c9b5f1114847fecc7ba39"));
//
//        Assert.assertNotNull(ret);
//    }

//    @SneakyThrows
//    @Test
//    public void getAncestorsTest() {
//        val client = getClient();
//        val ret = client.getAncestors(
//                Hash.from("9741d166fdc9b70a183d6c22f79e6f87c236f56c545c9b5f1114847fecc7ba39"), 5);
//
//        assertEquals(5, ret.stream().count());
//    }

//    @SneakyThrows
//    @Test
//    public void getBranchesTest() {
//        val client = getClient();
//        val ret = client.getBranches();
//
//        assertEquals(true, ret);
//    }

//    @SneakyThrows
//    @Test
//    public void invokeContractTest() {
//        val client = getClient();
//        val ret = client.invokeContract(
//                Hash.from("38c66a1cda6faa8b742d711cda13bfe9f1d04cb8f7261fa652f18422728f5642"),
//                ContractContext.builder()
//                        .contract(new ContractAddress(789, 0))
//                        .method("CIS2-NFT.view")
//                        .invoker(Optional.of(
//                                AddressAccount.from(
//                                        com.concordium.sdk.transactions.AccountAddress.from("48x2Uo8xCMMxwGuSQnwbqjzKtVqK5MaUud4vG7QEUgDmYkV85e"))))
//                        .Parameter(InvokeContractParameter.from(new byte[0]))
//                        .build()
//        );
//
//        assertEquals(true, ret);
//    }

//    @SneakyThrows
//    @Test
//    public void peerConnectTest() {
//        val client = getClient();
//        val ret = client.peerConnect(InetSocketAddress.createUnresolved("127.0.0.1", 8080));
//
//        assertEquals(true, ret);
//    }

//    @SneakyThrows
//    @Test
//    public void getAccountNonFinalizedTransactionsTest() {
//        val client = getClient();
//        val ret = client
//                .getAccountNonFinalizedTransactions(AccountAddress.from("3ghVzRz9BbQbQhetPFFK9JGZXeWV6fkM8K64NWz8VkgHynxkoA"));
//
//        assertEquals(1, ret.stream().count());
//    }

//    @SneakyThrows
//    @Test
//    public void transactionStatusInBlock() {
//        val client = getClient();
//        val ret = client.getTransactionStatusInBlock(
//                Hash.from("ea88c209c40f5828aeedf3326f314f66b7adf49e754a94f29b72e9d334d82eb7"),
//                Hash.from("2f15e174a42ec63d68abd8597e69573cf83199aacbfb9dae03c255d35b84aafb")
//        );
//        assertEquals(Status.FINALIZED, ret.getStatus());
//        assertTrue(ret.getResult().isPresent());
//        assertEquals(
//                ret.getResult().get().getHash().asHex(),
//                "ea88c209c40f5828aeedf3326f314f66b7adf49e754a94f29b72e9d334d82eb7");
//    }

//    @SneakyThrows
//    @Test
//    public void shutdownTest() {
//        val client = getClient();
//        val ret = client.shutdown();
//        assertEquals(true, ret);
//    }

//    @SneakyThrows
//    @Test
//    public void stopBakerTest() {
//        val client = getClient();
//        val ret = client.stopBaker();
//        assertEquals(true, ret);
//    }
//
//    @SneakyThrows
//    @Test
//    public void startBakerTest() {
//        val client = getClient();
//        val ret = client.startBaker();
//        assertEquals(true, ret);
//    }

//    @SneakyThrows
//    @Test
//    public void joinNetworkTest() {
//        val client = getClient();
//        val ret = client.getTotalReceived();
//        assertEquals(10, ret);
//    }

//    @SneakyThrows
//    @Test
//    public void joinNetworkTest() {
//        val client = getClient();
//        val ret = client.joinNetwork(UInt16.from(200));
//        assertEquals(true, ret);
//    }
//
//    @SneakyThrows
//    @Test
//    public void leaveNetworkTest() {
//        val client = getClient();
//        val ret = client.leaveNetwork(UInt16.from(200));
//        assertEquals(true, ret);
//    }

//    @SneakyThrows
//    @Test
//    public void banNodeIdTest() {
//        val client = getClient();
//        val peers = client.getPeerList(false);
//        val nodeId = peers.stream().findFirst().get().getNodeId();
//        val ret = client.banNode(BanNodeRequest.from(nodeId));
//
//        assertEquals(true, ret);
//    }
//
//    @SneakyThrows
//    @Test
//    public void banNodeIpTest() {
//        val client = getClient();
//        val peers = client.getPeerList(false);
//        val ipAddress = peers.stream().findFirst().get().getIpAddress();
//        val ret = client.banNode(BanNodeRequest.from(ipAddress));
//
//        assertEquals(true, ret);
//    }
//
//    @SneakyThrows
//    @Test
//    public void unBanNodeIpTest() {
//        val client = getClient();
//        val peers = client.getPeerList(false);
//        val ipAddress = peers.stream().findFirst().get().getIpAddress();
//        val ret = client.unBanNode(InetAddress.getByName("127.0.0.1"));
//
//        assertEquals(true, ret);
//    }

    private Client getClient() throws ClientInitializationException {
        Connection connection = Connection.builder()
                .credentials(Credentials.from("rpcadmin"))
                .host("127.0.0.1")
                .port(10001)
                .timeout(15000)
                .build();
        return Client.from(connection);
    }
}
