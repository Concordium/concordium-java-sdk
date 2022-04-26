package com.concordium.sdk.responses.blocksummary;

import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
public final class FinalizationReward {
    private CCDAmount amount;
    private AccountAddress address;

    @JsonCreator
    FinalizationReward(@JsonProperty("amount") String amount,
                       @JsonProperty("address") String address) {
        if (!Objects.isNull(amount)) {
            this.amount = CCDAmount.fromMicro(amount);
        }
        if (!Objects.isNull(address)) {
            this.address = AccountAddress.from(address);
        }
    }
}
