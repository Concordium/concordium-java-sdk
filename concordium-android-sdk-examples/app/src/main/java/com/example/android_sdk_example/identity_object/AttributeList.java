package com.example.android_sdk_example.identity_object;

import java.util.Map;

public class AttributeList {
    private Map<String, String> chosenAttributes;
    String validTo;
    String createdAt;
    int maxAccounts;

    public Map<String, String> getChosenAttributes() {
        return chosenAttributes;
    }

    public String getValidTo() {
        return validTo;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getMaxAccounts() {
        return maxAccounts;
    }
}
