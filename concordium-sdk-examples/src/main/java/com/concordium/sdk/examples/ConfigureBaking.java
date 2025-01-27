package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.crypto.bakertransactions.BakerKeys;
import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.requests.AccountQuery;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.responses.accountinfo.AccountInfo;
import com.concordium.sdk.responses.blockitemstatus.FinalizedBlockItem;
import com.concordium.sdk.responses.transactionstatus.OpenStatus;
import com.concordium.sdk.responses.transactionstatus.PartsPerHundredThousand;
import com.concordium.sdk.transactions.*;
import com.concordium.sdk.types.AccountAddress;
import lombok.val;
import picocli.CommandLine;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * An example of how to configure baking for an account.
 */
@CommandLine.Command(name = "ConfigureBaking", mixinStandardHelpOptions = true)
public class ConfigureBaking implements Callable<Integer> {

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
        BakerKeys bakerkeys = BakerKeys.createBakerKeys();
        System.out.println(bakerkeys.toJson());
        val tx = TransactionFactory.newConfigureBaker()
                .nonce(accountInfo.getNonce())
                .expiry(Expiry.createNew().addMinutes(5))
                .signer(signer)
                .sender(sender)
                .payload(ConfigureBakerPayload.builder()
                        .capital(CCDAmount.from(10000))
                        .openForDelegation(OpenStatus.OPEN_FOR_ALL)
                        .restakeEarnings(true)
                        .bakingRewardCommission(PartsPerHundredThousand.from(5000))
                        .finalizationRewardCommission(PartsPerHundredThousand.from(100000))
                        .transactionFeeCommission(PartsPerHundredThousand.from(80000))
                        .metadataUrl("")
                        .keysWithProofs(
                                ConfigureBakerKeysPayload
                                        .getNewConfigureBakerKeysPayload(sender, bakerkeys))
                        .suspended(false)
                        .build())
                .build();
        Hash hash = client.sendTransaction(tx);
        System.out.println(hash);
        Optional<FinalizedBlockItem> finalizedBlockItem = client.waitUntilFinalized(hash, this.timeout);
        System.out.println(finalizedBlockItem);

        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new ConfigureBaking()).execute(args);
        System.exit(exitCode);
    }
}
