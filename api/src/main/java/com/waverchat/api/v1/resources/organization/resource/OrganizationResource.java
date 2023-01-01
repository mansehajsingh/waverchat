package com.waverchat.api.v1.resources.organization.resource;

import com.waverchat.api.v1.exceptions.ConflictException;
import com.waverchat.api.v1.exceptions.NotFoundException;
import com.waverchat.api.v1.exceptions.ValidationException;
import com.waverchat.api.v1.framework.AbstractResource;
import com.waverchat.api.v1.resources.organization.dto.request.OrgCreateRQ;
import com.waverchat.api.v1.resources.organization.entity.Organization;
import com.waverchat.api.v1.resources.organization.service.OrganizationService;
import com.waverchat.api.v1.resources.organizationmember.service.OrganizationMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.waverchat.api.v1.util.Constants.BASE_URL;

@RestController
@RequestMapping(BASE_URL + "/organizations")
public class OrganizationResource extends AbstractResource {

    @Autowired
    private OrganizationService organizationService;

    @PostMapping
    public ResponseEntity create(@RequestBody OrgCreateRQ createRQ, HttpServletRequest request)
            throws ValidationException, ConflictException, NotFoundException {
        Long requestingUserId = this.authContext.getRequestingUserId();

        Organization organization = createRQ.toEntity();

        this.organizationService.create(organization, requestingUserId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
