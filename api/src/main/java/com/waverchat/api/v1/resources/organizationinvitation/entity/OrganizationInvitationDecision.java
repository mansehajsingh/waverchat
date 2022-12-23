package com.waverchat.api.v1.resources.organizationinvitation.entity;

public enum OrganizationInvitationDecision {

    ACCEPT,

    REJECT,

    UNKNOWN;

    public static OrganizationInvitationDecision parse(String value) {
        switch(value) {
            case "ACCEPT":
                return ACCEPT;
            case "REJECT":
                return REJECT;
            default:
                return UNKNOWN;
        }
    }

}
