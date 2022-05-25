package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountType;
import lombok.Getter;
import lombok.val;

import java.util.Map;

/**
 * An abstract Address.
 * Implementations are either Account - or Contract addresses.
 */
public abstract class AbstractAddress {

    @Getter
    private final AccountType type;

    AbstractAddress(AccountType type) {
        this.type = type;
    }

    //not pretty - find a better way of handling this.
    static AbstractAddress parseAccount(Map<String, Object> o) {
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
