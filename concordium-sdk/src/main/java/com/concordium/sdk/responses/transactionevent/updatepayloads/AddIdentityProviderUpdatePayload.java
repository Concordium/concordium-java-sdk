package com.concordium.sdk.responses.transactionevent.updatepayloads;

import com.concordium.grpc.v2.IpInfo;
import com.concordium.sdk.responses.blocksummary.updates.queues.IdentityProviderInfo;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class AddIdentityProviderUpdatePayload implements UpdatePayload {

    /**
     * Info of the added identity provider.
     */
    private IdentityProviderInfo identityProviderInfo;

    /**
     * Parses {@link IpInfo} to {@link AddIdentityProviderUpdatePayload}.
     * @param ipInfo {@link IpInfo} returned by the GRPC V2 API.
     * @return parsed {@link AddIdentityProviderUpdatePayload}.
     */
    public static AddIdentityProviderUpdatePayload parse(IpInfo ipInfo) {
        return AddIdentityProviderUpdatePayload.builder()
                .identityProviderInfo(IdentityProviderInfo.parse(ipInfo))
                .build();
    }

    @Override
    public UpdateType getType() {
        return UpdateType.ADD_IDENTITY_PROVIDER_UPDATE;
    }
}
