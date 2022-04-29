package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class BakerSetOpenStatus extends AbstractBakerResult {
    private final OpenStatus openStatus;

    @JsonCreator
    BakerSetOpenStatus(@JsonProperty("bakerId") String bakerId,
                       @JsonProperty("account") String bakerAccount,
                       @JsonProperty("openStatus") OpenStatus openStatus) {
        super(bakerId, bakerAccount);
        this.openStatus = openStatus;
    }
}
