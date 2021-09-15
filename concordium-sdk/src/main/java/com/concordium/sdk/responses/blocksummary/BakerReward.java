package com.concordium.sdk.responses.blocksummary;

import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.GTUAmount;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
public class BakerReward {
    private GTUAmount amount;
    private AccountAddress address;

    @JsonCreator
    BakerReward(@JsonProperty("amount") String amount,
                @JsonProperty("address") String address) {
        if (!Objects.isNull(amount)) {
            this.amount = GTUAmount.fromMicro(amount);
        }
        if (!Objects.isNull(address)) {
            this.address = AccountAddress.from(address);
        }
    }
}
