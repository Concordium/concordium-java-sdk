package com.concordium.sdk.examples;

import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.transactions.Index;
import com.concordium.sdk.transactions.MessageSigningDigest;
import com.concordium.sdk.transactions.SignerEntry;
import com.concordium.sdk.transactions.TransactionSigner;
import com.concordium.sdk.types.AccountAddress;
import lombok.val;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import picocli.CommandLine;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * Signs the given message in the same way the transactions are signed.
 */
@CommandLine.Command(name = "SignMessage", mixinStandardHelpOptions = true)
public class SignMessage implements Callable<Integer> {
    @CommandLine.Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "http://localhost:20001")
    private String endpoint;

    @CommandLine.Option(
            names = "--hex",
            description = "Whether the given file contains HEX-encoded data")
    private boolean isHex;

    @CommandLine.Parameters(
            index = "0",
            description = "A file containing the message"
    )
    private Path messageFilePath;

    @Override
    public Integer call() throws Exception {
        val signer = TransactionSigner.from(
                SignerEntry.from(
                        Index.from(0),
                        Index.from(0),
                        ED25519SecretKey.from("7100071c835a0a35e86dccba7ee9d10b89e36d1e596771cdc8ee36a17f7abbf2")
                )
        );
        val address = AccountAddress.from("3WZE6etUvVp1eyhEtTxqZrQaanTAZnZCHEmZmDyCbCwxnmQuPE");
        val fileContents = Files.readAllBytes(messageFilePath);
        System.out.println(messageFilePath);
        val message = (isHex) ? Hex.decodeHex(new String(fileContents)) : fileContents;
        val digest = MessageSigningDigest.from(address, message);
        val signature = signer.sign(digest);
        System.out.println(Hex.encodeHexString(
                Objects.requireNonNull(
                                Objects.requireNonNull(signature.getSignatures().firstEntry())
                                        .getValue()
                                        .getSignatures()
                                        .firstEntry()
                        )
                        .getValue()
                        .getBytes()
        ));
        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new SignMessage()).execute(args);
        System.exit(exitCode);
    }
}
