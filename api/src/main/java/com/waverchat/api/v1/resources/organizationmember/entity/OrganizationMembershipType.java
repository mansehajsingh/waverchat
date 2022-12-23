package com.waverchat.api.v1.resources.organizationmember.entity;

public enum OrganizationMembershipType {
    
    OWNER("OWNER"),
    REGULAR("REGULAR");

    private String value;

    OrganizationMembershipType(String value){
        this.value = value;
    }

    public static OrganizationMembershipType parse(String value) {
        OrganizationMembershipType returnType = null;

        for (OrganizationMembershipType type : OrganizationMembershipType.values()) {
            if (type.getValue().equals(value)) {
                returnType = type;
                break;
            }
        }

        return returnType;
    }

    public String getValue() {
        return this.value;
    }
    
}
