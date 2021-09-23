package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountType;
import lombok.val;

import java.util.Map;

/**
 * Base class for {@link com.concordium.sdk.transactions.AccountType}
 */
abstract class AbstractAccount {

    //not pretty - find a better way of handling this.
    static AbstractAccount parseAccount(Map<String, Object> o) {
        try {
            if (isContractAddress(o)) {
                val contract = (Map<String, Integer>) o.get("address");
                return new ContractAddress(
                        contract.get("subindex"),
                        contract.get("index"),
                        AccountType.from(((String) o.get("type"))));
            } else {
                return new Account(
                        AccountType.from(
                                ((String) o.get("type"))),
                        ((String) o.get("address")));
            }
        } catch (Exception e) {
            return null;
        }
    }

    private static boolean isContractAddress(Map<String, Object> o) {
        return AccountType.from((String) o.get("type")) == AccountType.ADDRESS_CONTRACT;
    }

}
