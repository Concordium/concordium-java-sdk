package com.concordium.sdk.examples.contractexample.cis2nft;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.requests.AccountQuery;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.responses.modulelist.ModuleRef;
import com.concordium.sdk.transactions.*;
import com.concordium.sdk.transactions.smartcontracts.SchemaParameter;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.ContractAddress;
import com.concordium.sdk.types.Nonce;
import com.concordium.sdk.types.UInt64;
import lombok.SneakyThrows;
import lombok.var;
import picocli.CommandLine;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Callable;

/**
 * Calls different methods on a <a href="https://github.com/Concordium/concordium-rust-smart-contracts/blob/main/examples/cis2-nft/src/lib.rs">cis2-nft smart contract</a> deployed on the chain.
 * See {@link Cis2NftParameters} for how to create and initialize custom smart contract parameters.
 * SENDER_ADDRESS, MODULE_REF, CONTRACT_ADDRESS and the key in SIGNER are dummy values and should be replaced
 */
@CommandLine.Command(name = "Cis2Nft", mixinStandardHelpOptions = true)
public class Cis2Nft implements Callable<Integer> {

    private static final String SENDER_ADDRESS = "3WZE6etUvVp1eyhEtTxqZrQaanTAZnZCHEmZmDyCbCwxnmQuPE"; //  Dummy address
    private static final ModuleRef MODULE_REF = ModuleRef.from("247a7ac6efd2e46f72fd18741a6d1a0254ec14f95639df37079a576b2033873e"); // Dummy module ref
    private static final ContractAddress CONTRACT_ADDRESS = ContractAddress.from(1, 0); // Dummy contract address

    private static final Expiry EXPIRY = Expiry.createNew().addMinutes(5);

    private static final TransactionSigner SIGNER = TransactionSigner.from(
            SignerEntry.from(Index.from(0), Index.from(0), // TODO dummy key
                    ED25519SecretKey.from("56f60de843790c308dac2d59a5eec9f6b1649513f827e5a13d7038accfe31784")) // Dummy key
    );

    @CommandLine.Option(
            names = {"-m", "--method"},
            required = true,
            description = "Name of method. Valid names are: ${COMPLETION-CANDIDATES}")
    private Cis2NftMethod methodName;

    @CommandLine.Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "http://localhost:20002")
    private String endpoint;

    @CommandLine.Option(
            names = {"--timeout"},
            description = "GRPC request timeout in milliseconds.",
            defaultValue = "100000")
    private int timeout;


    @Override
    public Integer call() throws IOException, ClientInitializationException {
        var endpointUrl = new URL(this.endpoint);
        Connection connection = Connection.newBuilder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .timeout(timeout)
                .build();
        var client = ClientV2.from(connection);
        Nonce nonce = client.getAccountInfo(BlockQuery.BEST, AccountQuery.from(AccountAddress.from(SENDER_ADDRESS))).getAccountNonce();

        switch (this.methodName) {
            case INIT:
                handleInit(client, nonce);
                break;
            case MINT:
                SchemaParameter mintParams = Cis2NftParameters.generateMintParams();
                handleMethod(client, nonce, mintParams);
                break;
            case TRANSFER:
                SchemaParameter transferParams = Cis2NftParameters.generateTransferParams();
                handleMethod(client, nonce, transferParams);
                break;
            case UPDATE_OPERATOR:
                SchemaParameter updateOperatorParams = Cis2NftParameters.generateUpdateOperatorParams();
                handleMethod(client, nonce, updateOperatorParams);
                break;
            case OPERATOR_OF:
                SchemaParameter operatorOfParams = Cis2NftParameters.generateOperatorOfParams();
                handleMethod(client, nonce, operatorOfParams);
                break;
            case BALANCE_OF:
                SchemaParameter balanceOfParams = Cis2NftParameters.generateBalanceOfParams();
                handleMethod(client, nonce, balanceOfParams);
                break;
            case TOKEN_METADATA:
                SchemaParameter tokenMetadataParams = Cis2NftParameters.generateTokenMetadataParams();
                handleMethod(client, nonce, tokenMetadataParams);
                break;
            case SUPPORTS:
                SchemaParameter supportsParams = Cis2NftParameters.generateSupportsParameter();
                handleMethod(client, nonce, supportsParams);
                break;
            case SET_IMPLEMENTORS:
                SchemaParameter setImplementorsParams = Cis2NftParameters.generateSetImplementorsParams();
                handleMethod(client, nonce, setImplementorsParams);
                break;
        }
        return 0;
    }

    private void handleInit(ClientV2 client, Nonce nonce) {
        InitName initName = InitName.from("init_cis2_nft");
        InitContractPayload payload = InitContractPayload.from(CCDAmount.fromMicro(0), MODULE_REF, initName, Parameter.EMPTY);
        InitContractTransaction initContractTransaction = TransactionFactory.newInitContract()
                .sender(AccountAddress.from(SENDER_ADDRESS))
                .payload(payload)
                .expiry(EXPIRY)
                .nonce(AccountNonce.from(nonce))
                .signer(SIGNER)
                .maxEnergyCost(UInt64.from(10000))
                .build();
        Hash txHash = client.sendTransaction(initContractTransaction);
        System.out.println("Submitted transaction for " + this.methodName  + " with hash: " + txHash);
        sleep();
        Hash blockHash = client.getBlockItemStatus(txHash).getFinalizedBlockItem().get().getBlockHash();
        System.out.println("Transaction finalized in block with hash: " + blockHash);
    }

    private void handleMethod(ClientV2 client, Nonce nonce, SchemaParameter parameter) {
        UpdateContractPayload payload = UpdateContractPayload.from(CONTRACT_ADDRESS, parameter);
        UpdateContractTransaction transaction = TransactionFactory.newUpdateContract()
                .sender(AccountAddress.from(SENDER_ADDRESS))
                .payload(payload)
                .expiry(EXPIRY)
                .nonce(AccountNonce.from(nonce))
                .signer(SIGNER)
                .maxEnergyCost(UInt64.from(10000))
                .build();
        Hash txHash = client.sendTransaction(transaction);
        System.out.println("Submitted transaction for " + this.methodName  + " with hash: " + txHash);
        sleep();
        Hash blockHash = client.getBlockItemStatus(txHash).getFinalizedBlockItem().get().getBlockHash();
        System.out.println("Transaction finalized in block with hash: " + blockHash);
    }

    @SneakyThrows
    private void sleep() {
        for (int i = 0; i < 200; i++) {
            System.out.print(".");
            if (i % 50 == 0) {
                System.out.println("\n");
            }
            Thread.sleep(30);
        }
    }
    public static void main(String[] args) {
        int exitCode = new CommandLine(new Cis2Nft()).execute(args);
        System.exit(exitCode);
    }
}
