package com.waverchat.api.v1.resources.usercreationconfirmation.resource;

import com.waverchat.api.v1.exceptions.ConflictException;
import com.waverchat.api.v1.exceptions.ValidationException;
import com.waverchat.api.v1.framework.AbstractResource;
import com.waverchat.api.v1.resources.usercreationconfirmation.dto.request.UCCCreateRQ;
import com.waverchat.api.v1.resources.usercreationconfirmation.dto.response.UCCCreateRS;
import com.waverchat.api.v1.resources.usercreationconfirmation.entity.UserCreationConfirmation;
import com.waverchat.api.v1.resources.usercreationconfirmation.service.UCCService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.waverchat.api.v1.util.Constants.BASE_URL;

@RestController
@RequestMapping(BASE_URL + "/user-creation-confirmations")
public class UCCResource extends AbstractResource {

    protected final static Logger log = LoggerFactory.getLogger(UCCResource.class);

    @Autowired
    protected UCCService uccService;

    @PostMapping
    public ResponseEntity<UCCCreateRS> create(
            @RequestBody UCCCreateRQ createRequest
    )
            throws ValidationException, ConflictException
    {
        this.uccService.auditForCreate(createRequest);
        UserCreationConfirmation createdUCC = this.uccService.create(createRequest);

        UCCCreateRS responseBody = UCCCreateRS.from(createdUCC);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseBody);
    }

}
