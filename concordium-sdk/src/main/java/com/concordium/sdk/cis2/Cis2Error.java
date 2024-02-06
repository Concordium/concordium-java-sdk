package com.concordium.sdk.cis2;

import com.concordium.sdk.responses.transactionstatus.RejectReason;
import com.concordium.sdk.responses.transactionstatus.RejectReasonRejectedReceive;
import com.concordium.sdk.responses.transactionstatus.RejectReasonType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;


/**
 * Errors supported by the CIS2 standard https://proposals.concordium.software/CIS/cis-2.html#rejection-errors
 */
@EqualsAndHashCode
@ToString
@Getter
public class Cis2Error {

    /**
     * Type of the error.
     * See {@link Cis2Error.Type} for standardized variants.
     */
    private final Type type;

    private final int rawErrorCode;

    Cis2Error(Type type, int rawErrorCode) {
        this.type = type;
        this.rawErrorCode = rawErrorCode;
    }

    public static Cis2Error from(int errorCode) {
        if (errorCode == -42000001) return new Cis2Error(Type.INVALID_TOKEN_ID, errorCode);
        if (errorCode == -42000002) return new Cis2Error(Type.INSUFFICIENT_FUNDS, errorCode);
        if (errorCode == -42000003) return new Cis2Error(Type.UNAUTHORIZED, errorCode);
        return new Cis2Error(Type.CUSTOM, errorCode);
    }


    /**
     * Parse a CIS2Error from the provided reject reason.
     * <p>
     * The provided reject reason MUST be of type {@link RejectReasonType#REJECTED_RECEIVE}
     *
     * @param rejectReason the reject reason
     * @return the parsed {@link Cis2Error}
     */
    static Cis2Error from(RejectReason rejectReason) {
        return Cis2Error.from(((RejectReasonRejectedReceive) rejectReason).getRejectReason());
    }

    public enum Type {
        INVALID_TOKEN_ID,
        INSUFFICIENT_FUNDS,
        UNAUTHORIZED,
        CUSTOM
    }
}
