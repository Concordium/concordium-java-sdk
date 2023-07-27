package com.concordium.sdk.types;

import com.concordium.grpc.v2.Address;
import com.concordium.sdk.transactions.AccountType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.val;

import java.util.Map;

/**
 * An abstract Address.
 * Implementations are either Account - or Contract addresses.
 */
@EqualsAndHashCode
public abstract class AbstractAddress {

    @Getter
    private final AccountType type;

    AbstractAddress(AccountType type) {
        this.type = type;
    }

    //not pretty - find a better way of handling this.
    public static AbstractAddress parseAccount(Map<String, Object> o) {
        try {
            if (isContractAddress(o)) {
                val contract = (Map<String, Integer>) o.get("address");
                return new ContractAddress(
                        contract.get("subindex"),
                        contract.get("index"));
            } else {
                return AccountAddress.from(((String) o.get("address")));
            }
        } catch (Exception e) {
            return null;
        }
    }

    private static boolean isContractAddress(Map<String, Object> o) {
        return AccountType.from((String) o.get("type")) == AccountType.ADDRESS_CONTRACT;
    }

    public static AbstractAddress from(Address instigator) {
        switch (instigator.getTypeCase()){
            case ACCOUNT:
                return AccountAddress.from(instigator.getAccount());
            case CONTRACT:
                return ContractAddress.from(instigator.getContract());
            case TYPE_NOT_SET:
                throw new IllegalArgumentException("Account type not set.");
        }
        throw new IllegalArgumentException("Unrecognized account type.");
    }

    @Override
    public String toString() {
        return this.type.toString();
    }

}
