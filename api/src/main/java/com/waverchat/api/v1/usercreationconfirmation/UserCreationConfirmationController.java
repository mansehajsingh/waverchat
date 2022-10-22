package com.waverchat.api.v1.usercreationconfirmation;

import com.waverchat.api.v1.exceptions.ConflictException;
import com.waverchat.api.v1.exceptions.ResourceNotFoundException;
import com.waverchat.api.v1.exceptions.ValidationException;
import com.waverchat.api.v1.http.response.MessageResponse;
import com.waverchat.api.v1.http.response.MultiMessageResponse;
import com.waverchat.api.v1.user.User;
import com.waverchat.api.v1.usercreationconfirmation.http.UserCreationConfirmationRequest;
import com.waverchat.api.v1.usercreationconfirmation.http.UserCreationConfirmationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user-creation-confirmations")
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
                userCreationConfirmationRequest.getPassword(),
                userCreationConfirmationRequest.getFirstName(),
                userCreationConfirmationRequest.getLastName()
        );

        try {
            this.userCreationConfirmationService.create(userCreationConfirmation);
        } catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse(e.getMessage()));
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new MultiMessageResponse(e.getMessages()));
        }

        // TODO: Send email template to requested email

        // typically we'd want to return the created resource, but due to the email verification system, we cannot
        // send back the resource because it will be sent to the email
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new MessageResponse("Created UserCreationConfirmation successfully.")
        );
    }

    @DeleteMapping("/{idAsString}")
    public ResponseEntity<?> confirmUser(@PathVariable String idAsString) {
        UUID id;

        try {
            id = UUID.fromString(idAsString);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new MessageResponse("Incorrect UUID format"));
        }

        User createdUser;

        try {
            createdUser = this.userCreationConfirmationService.deleteAllWithEmailUponConfirmation(id);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new UserCreationConfirmationResponse(
                createdUser.getId(),
                createdUser.getEmail(),
                createdUser.getUsername(),
                createdUser.getFirstName(),
                createdUser.getLastName()
        ));
    }

}
