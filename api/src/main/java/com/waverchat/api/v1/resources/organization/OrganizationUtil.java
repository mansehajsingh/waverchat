package com.waverchat.api.v1.resources.organization;

public class OrganizationUtil {

    public static boolean isValidName(String name) {
        if (name == null) return false;

        if (name.length() < OrganizationConstants.MAX_ORG_NAME_LENGTH
                || name.length() > OrganizationConstants.MAX_ORG_NAME_LENGTH)
        {
            return false;
        }

        return true;
    }

    public static boolean isValidDescription(String description) {
        if (description == null) return false;

        if (description.length() < OrganizationConstants.MAX_ORG_NAME_LENGTH
                || description.length() > OrganizationConstants.MAX_ORG_NAME_LENGTH)
        {
            return false;
        }

        return true;
    }

}
