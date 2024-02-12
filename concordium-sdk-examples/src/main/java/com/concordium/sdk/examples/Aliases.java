package com.concordium.sdk.examples;

import com.concordium.sdk.types.AccountAddress;
import picocli.CommandLine;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Utility to check whether a list of accounts are all aliases of each other.
 */
@CommandLine.Command(name = "Aliases", mixinStandardHelpOptions = true)
public class Aliases implements Callable<Integer> {

    @CommandLine.Parameters(
            description = "List of addresses to check",
            arity = "2..." )
    List<String> addresses;
    @Override
    public Integer call() throws Exception {
        String first = addresses.remove(0);
        AccountAddress addr = AccountAddress.from(first);
        for (String a : addresses) {

            if (!addr.isAliasOf(AccountAddress.from(a))) {
                System.out.println(a + " is not an alias of " + first);
                return 0;
            }
        }
        System.out.println("All addresses are aliases");
        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Aliases()).execute(args);
        System.exit(exitCode);
    }
}
