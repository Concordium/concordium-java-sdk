package com.concordium.sdk.examples.contractexample.wccd;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.examples.contractexample.cis2nft.Cis2NftParameters;
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

import java.net.URL;
import java.util.concurrent.Callable;

/**
 * Calls different methods on a <a href="https://github.com/Concordium/concordium-rust-smart-contracts/blob/main/examples/cis2-wccd/src/lib.rs">cis2-wCCD smart contract</a> deployed on the chain.
 * See {@link Cis2NftParameters} for how to create and initialize custom smart contract parameters.
 * TODO what values should user replace
 */
@CommandLine.Command(name = "Cis2WCCD", mixinStandardHelpOptions = true)
public class Cis2WCCD implements Callable<Integer> {
    private static final String SENDER_ADDRESS = "3WZE6etUvVp1eyhEtTxqZrQaanTAZnZCHEmZmDyCbCwxnmQuPE"; //  TODO dummy address
    private static final String PATH_TO_MODULE = "./src/main/java/com/concordium/sdk/examples/contractexample/cis2nft/cis2-nft.wasm.v1";
    private static final ContractAddress CONTRACT_ADDRESS = ContractAddress.from(1, 0); // Dummy contract address

    private static final Expiry EXPIRY = Expiry.createNew().addMinutes(5);

    private static final TransactionSigner SIGNER = TransactionSigner.from(
            SignerEntry.from(Index.from(0), Index.from(0), // TODO dummy key
                    ED25519SecretKey.from("56f60de843790c308dac2d59a5eec9f6b1649513f827e5a13d7038accfe31784"))
    );

    @CommandLine.Option(
            names = {"-m", "--method"},
            required = true,
            description = "Name of method. Valid names are: ${COMPLETION-CANDIDATES}")
    private Cis2WCCDMethod methodName;

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
    public Integer call() throws Exception {
        var endpointUrl = new URL(this.endpoint);
        Connection connection = Connection.newBuilder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .timeout(timeout)
                .build();
        var client = ClientV2.from(connection);
        //Nonce nonce = client.getAccountInfo(BlockQuery.BEST, AccountQuery.from(AccountAddress.from(SENDER_ADDRESS))).getAccountNonce();
        Nonce nonce = Nonce.from(1);

        switch (this.methodName) {
            case INIT:
                handleInit(client, nonce);
                break;
            case WRAP:
                SchemaParameter wrapParams = Cis2WCCDParameters.generateWrapParams();
                handleMethod(client, nonce, wrapParams);
                break;
            case UNWRAP:
                SchemaParameter unwrapParams = Cis2WCCDParameters.generateUnwrapParams();
                handleMethod(client, nonce, unwrapParams);
                break;
            case UPDATE_ADMIN:
                SchemaParameter updateAdminParams = Cis2WCCDParameters.generateUpdateAdminParams();
                handleMethod(client, nonce, updateAdminParams);
                break;
            case SET_PAUSED:
                SchemaParameter setPausedParams = Cis2WCCDParameters.generateSetPausedParams();
                handleMethod(client, nonce, setPausedParams);
                break;
            case SET_METADATA_URL:
                SchemaParameter setMetadataUrlParams = Cis2WCCDParameters.generateSetMetadataUrlParams();
                handleMethod(client, nonce, setMetadataUrlParams);
                break;
            case TRANSFER:
                SchemaParameter transferParams = Cis2WCCDParameters.generateTransferParams();
                handleMethod(client, nonce, transferParams);
                break;
            case UPDATE_OPERATOR:
                SchemaParameter updateOperatorParams = Cis2WCCDParameters.generateUpdateOperatorParams();
                handleMethod(client, nonce, updateOperatorParams);
                break;
            case BALANCE_OF:
                SchemaParameter balanceOfParams = Cis2WCCDParameters.generateBalanceOfParams();
                handleMethod(client, nonce, balanceOfParams);
                break;
            case OPERATOR_OF:
                SchemaParameter operatorOfParams = Cis2WCCDParameters.generateOperatorOfParams();
                handleMethod(client, nonce, operatorOfParams);
                break;
            case TOKEN_METADATA:
                SchemaParameter tokenMetadataParams = Cis2WCCDParameters.generateTokenMetadataParams();
                handleMethod(client, nonce, tokenMetadataParams);
                break;
            case SUPPORTS:
                SchemaParameter supportsParams = Cis2WCCDParameters.generateSupportsParams();
                handleMethod(client, nonce, supportsParams);
                break;
            case SET_IMPLEMENTORS:
                SchemaParameter setImplementorsParams = Cis2WCCDParameters.generateSetImplementorsParams();
                handleMethod(client, nonce, setImplementorsParams);
                break;
            case UPGRADE:
                SchemaParameter upgradeParams = Cis2WCCDParameters.generateUpgradeParams();
                handleMethod(client, nonce, upgradeParams);
                break;
        }
        return 0;
    }

    private void handleInit(ClientV2 client, Nonce nonce) {
        ModuleRef ref = null;
        InitName initName = InitName.from("init_cis2_wCCD");
        InitContractPayload payload = InitContractPayload.from(CCDAmount.fromMicro(0), ref, initName, Parameter.EMPTY);
        InitContractTransaction initContractTransaction = TransactionFactory.newInitContract()
                .sender(AccountAddress.from(SENDER_ADDRESS))
                .payload(payload)
                .expiry(EXPIRY)
                .nonce(AccountNonce.from(nonce))
                .signer(SIGNER)
                .maxEnergyCost(UInt64.from(10000))
                .build();
        //Hash txHash = client.sendTransaction(initContractTransaction);
        //System.out.println("Submitted transaction for " + this.methodName  + " with hash: " + txHash);
        //sleep();
        //Hash blockHash = client.getBlockItemStatus(txHash).getFinalizedBlockItem().get().getBlockHash();
        //System.out.println("Transaction finalized in block with hash: " + blockHash);
        System.out.println(this.methodName);

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
        //Hash txHash = client.sendTransaction(transaction);
        //System.out.println("Submitted transaction for " + this.methodName  + " with hash: " + txHash);
        //sleep();
        //Hash blockHash = client.getBlockItemStatus(txHash).getFinalizedBlockItem().get().getBlockHash();
        //System.out.println("Transaction finalized in block with hash: " + blockHash);
        System.out.println(this.methodName);



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
        int exitCode = new CommandLine(new Cis2WCCD()).execute(args);
        System.exit(exitCode);
    }
}
