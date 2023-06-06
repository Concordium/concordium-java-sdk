package com.concordium.sdk.types;

import com.concordium.grpc.v2.Address;
import com.concordium.sdk.transactions.AccountType;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Map;

import lombok.val;

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
                return AccountAddress.from((String) o.get("address"));
            }
        } catch (Exception e) {
            return null;
        }
    }

    private static boolean isContractAddress(Map<String, Object> o) {
        return AccountType.from((String) o.get("type")) == AccountType.ADDRESS_CONTRACT;
    }

    /**
     * Parses {@link Address} to {@link AbstractAddress}.
     *
     * @param address {@link Address} returned by the GRPC V2 API.
     * @return parsed {@link AbstractAddress}.
     */
    public static AbstractAddress parse(Address address) {
        switch (address.getTypeCase()) {
            case ACCOUNT:
                return AccountAddress.parse(address.getAccount());
            case CONTRACT:
                return ContractAddress.parse(address.getContract());
            default:
                throw new IllegalArgumentException();
        }
    }
}
