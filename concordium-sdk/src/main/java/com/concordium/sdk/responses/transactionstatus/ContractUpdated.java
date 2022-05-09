package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.GTUAmount;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@ToString
@Getter
public class ContractUpdated extends TransactionResultEvent {
    private GTUAmount amount;
    
    private final AbstractAccount instigator;
    private final ContractAddress address;
    private final String receiveName;
    private final List<String> events;
    private final String message;

    @JsonCreator
    ContractUpdated(@JsonProperty("amount") String amount,
                    @JsonProperty("instigator") Map<String, Object> instigator,
                    @JsonProperty("address") Map<String, Object> address,
                    @JsonProperty("receiveName") String receiveName,
                    @JsonProperty("events") List<String> events,
                    @JsonProperty("message") String message) {
        this.instigator = AbstractAccount.parseAccount(instigator);
        this.address = (ContractAddress) AbstractAccount.parseAccount(address);
        this.receiveName = receiveName;
        this.events = events;
        this.message = message;
        if (!Objects.isNull(amount)) {
            this.amount = GTUAmount.fromMicro(amount);
        }
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.CONTRACT_UPDATED;
    }
}
