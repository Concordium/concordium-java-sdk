package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.requests.AccountQuery;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.responses.accountinfo.AccountInfo;
import com.concordium.sdk.responses.blockitemstatus.FinalizedBlockItem;
import com.concordium.sdk.responses.transactionstatus.DelegationTarget;
import com.concordium.sdk.transactions.*;
import com.concordium.sdk.types.AccountAddress;
import lombok.val;
import picocli.CommandLine;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * An example of how to configure delegation for an account.
 */
@CommandLine.Command(name = "ConfigureDelegation", mixinStandardHelpOptions = true)
public class ConfigureDelegation implements Callable<Integer> {

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
    public Integer call() throws MalformedURLException, ClientInitializationException {
        val endpointUrl = new URL(this.endpoint);

        Connection connection = Connection.newBuilder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .build();
        val client = ClientV2.from(connection);

        AccountAddress sender = AccountAddress.from("3WZE6etUvVp1eyhEtTxqZrQaanTAZnZCHEmZmDyCbCwxnmQuPE");
        TransactionSigner signer = TransactionSigner.from(
                SignerEntry.from(Index.from(0), Index.from(0),
                        ED25519SecretKey
                                .from("56f60de843790c308dac2d59a5eec9f6b1649513f827e5a13d7038accfe31784")));
        AccountInfo accountInfo = client.getAccountInfo(BlockQuery.LAST_FINAL, AccountQuery.from(sender));
        val tx = TransactionFactory
                .newConfigureDelegation(
                        com.concordium.sdk.transactions.ConfigureDelegation.builder()
                                .capital(CCDAmount.from(200))
                                .delegationTarget(DelegationTarget.builder()
                                        .type(DelegationTarget.DelegationType.BAKER)
                                        .bakerId(BakerId.from(207))
                                        .build())
                                .build()
                )
                .nonce(accountInfo.getNonce())
                .expiry(Expiry.createNew().addMinutes(5))
                .sender(sender)
                .sign(signer);
        Hash hash = client.sendTransaction(tx);
        System.out.println(hash);
        Optional<FinalizedBlockItem> finalizedBlockItem = client.waitUntilFinalized(hash, this.timeout);
        System.out.println(finalizedBlockItem);

        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new ConfigureDelegation()).execute(args);
        System.exit(exitCode);
    }
}
