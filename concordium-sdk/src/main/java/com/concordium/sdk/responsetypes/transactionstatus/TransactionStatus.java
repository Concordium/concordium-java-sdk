package com.concordium.sdk.responsetypes.transactionstatus;

import com.concordium.sdk.serializing.JsonMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
public class TransactionStatus {
    private Status status;
    private Map<String, TransactionSummary> outcomes;

    @SneakyThrows
    public static TransactionStatus fromJson(String transactionStatus) {
        return JsonMapper.MAPPER.readValue(transactionStatus, TransactionStatus.class);
    }
}
