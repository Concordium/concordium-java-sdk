package com.concordium.sdk.responses.transactionevent.updatepayloads;


import com.concordium.sdk.transactions.AccountAddress;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class FoundationAccountUpdatePayload implements UpdatePayload{


    private AccountAddress accountAddress;

    /**
     * Parses {@link com.concordium.grpc.v2.AccountAddress} to {@link FoundationAccountUpdatePayload}.
     * @param accountAddress {@link com.concordium.grpc.v2.AccountAddress} returned by the GRPC V2 API.
     * @return parsed {@link com.concordium.grpc.v2.AccountAddress}.
     */
    public static FoundationAccountUpdatePayload parse(com.concordium.grpc.v2.AccountAddress accountAddress) {
        return FoundationAccountUpdatePayload.builder()
                .accountAddress(AccountAddress.from(accountAddress.getValue().toByteArray()))
                .build();
    }
    @Override
    public UpdateType getType() {
        return UpdateType.FOUNDATION_ACCOUNT_UPDATE;
    }
}
