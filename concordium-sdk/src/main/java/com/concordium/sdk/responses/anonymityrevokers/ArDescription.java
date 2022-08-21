package com.concordium.sdk.responses.anonymityrevokers;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

/**
 * Description of the anonymity revoker (e.g. name, contact number).
 */
@Getter
@Data
@ToString
public class ArDescription {
    public String name;
    public String url;
    public String description;
}
