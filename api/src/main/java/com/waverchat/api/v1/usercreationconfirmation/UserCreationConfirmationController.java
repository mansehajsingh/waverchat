package com.waverchat.api.v1.usercreationconfirmation;

import com.waverchat.api.v1.exceptions.ConflictException;
import com.waverchat.api.v1.http.response.MessageResponse;
import com.waverchat.api.v1.usercreationconfirmation.UserCreationConfirmation;
import com.waverchat.api.v1.usercreationconfirmation.UserCreationConfirmationService;
import com.waverchat.api.v1.usercreationconfirmation.http.UserCreationConfirmationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/user-creation-confirmation")
public class UserCreationConfirmationController {

    @Autowired
    UserCreationConfirmationService userCreationConfirmationService;

    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody UserCreationConfirmationRequest userCreationConfirmationRequest
    ) {
        UserCreationConfirmation userCreationConfirmation = new UserCreationConfirmation(
                userCreationConfirmationRequest.getEmail(),
                userCreationConfirmationRequest.getUsername(),
                userCreationConfirmationRequest.getPassword(), // the service implementation will hash it
                userCreationConfirmationRequest.getFirstName(),
                userCreationConfirmationRequest.getLastName(),
                false,
                false
        );

        try {
            userCreationConfirmationService.create(userCreationConfirmation);
        } catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse(e.getMessage()));
        }

        // TODO: Send email template to requested email

        // typically we'd want to return the created resource, but due to the email verification system, we cannot
        // send back the resource because it will be sent to the email
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new MessageResponse("Created UserCreationConfirmation successfully.")
        );
    }

}
