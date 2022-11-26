package com.waverchat.api.v1.resources.organization;

import com.waverchat.api.v1.customframework.AbstractApplicationEntity;
import com.waverchat.api.v1.customframework.RequestProperties;
import com.waverchat.api.v1.exceptions.ValidationException;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "organizations")
@Data
public class Organization extends AbstractApplicationEntity {

    @NotNull
    @Length(min=OrganizationConstants.MIN_ORG_NAME_LENGTH, max=OrganizationConstants.MAX_ORG_NAME_LENGTH)
    private String name;

    @NotNull
    @Length(min=OrganizationConstants.MIN_ORG_DESC_LENGTH, max=OrganizationConstants.MAX_ORG_DESC_LENGTH)
    private String description;

    public Organization() {}

    public Organization(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Organization(RequestProperties props) {
        this.name = (String) props.getRequestBody().get("name");
        this.description = (String) props.getRequestBody().get("description");
    }

    @Override
    public void validateForCreate() throws ValidationException {
        List<String> validationExceptionMessages = new ArrayList<>();

        if (!OrganizationUtil.isValidName(this.name)) {
            validationExceptionMessages.add("Organization name is invalid.");
        }
        if (!OrganizationUtil.isValidDescription(this.description)) {
            validationExceptionMessages.add("Organization description is invalid");
        }

        // if there were any error messages added during validation we want to throw an exception
        // containing all those messages
        if (!validationExceptionMessages.isEmpty()) {
            throw new ValidationException(validationExceptionMessages);
        }
    }
}
