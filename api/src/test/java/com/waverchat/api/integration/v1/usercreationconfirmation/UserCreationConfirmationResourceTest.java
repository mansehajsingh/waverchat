package com.waverchat.api.integration.v1.usercreationconfirmation;

import com.google.gson.Gson;
import com.mysema.commons.lang.Pair;
import com.waverchat.api.integration.v1.util.App;
import com.waverchat.api.integration.v1.util.TestUtils;
import com.waverchat.api.v1.resources.user.entity.User;
import com.waverchat.api.v1.resources.user.UserConstants;
import com.waverchat.api.v1.resources.user.UserRepository;
import com.waverchat.api.v1.resources.usercreationconfirmation.entity.UserCreationConfirmation;
import com.waverchat.api.v1.resources.usercreationconfirmation.UserCreationConfirmationRepository;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserCreationConfirmationResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserCreationConfirmationRepository userCreationConfirmationRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void checkCreateResponseAndUCCPersistedCorrectly() throws Exception {

        Pair<UserCreationConfirmation, ResultActions> genPair = App.generateAndSendUserCreationConfirmation(
                new UserCreationConfirmation(), this.mockMvc);

        UserCreationConfirmation generatedUCC = genPair.getFirst();
        ResultActions creationResult = genPair.getSecond();

        creationResult.andExpect(status().isCreated());

        Gson gson = new Gson();

        Map<String, String> response =
                gson.fromJson(creationResult.andReturn().getResponse().getContentAsString(), Map.class);

        assert response.containsKey("email");
        assertEquals(generatedUCC.getEmail(), response.get("email"));

        assert response.containsKey("username");
        assertEquals(generatedUCC.getUsername(), response.get("username"));

        assert response.containsKey("firstName");
        assertEquals(generatedUCC.getFirstName(), response.get("firstName"));

        assert response.containsKey("lastName");
        assertEquals(generatedUCC.getLastName(), response.get("lastName"));

        Optional<UserCreationConfirmation> uccOpt =
                this.userCreationConfirmationRepository.findByUsernameIgnoreCase(generatedUCC.getUsername());

        assert uccOpt.isPresent();

        UserCreationConfirmation createdUCC = uccOpt.get();

        assertEquals(generatedUCC.getEmail(), createdUCC.getEmail());
        assertEquals(generatedUCC.getUsername(), createdUCC.getUsername());
        assertEquals(generatedUCC.getFirstName(), createdUCC.getFirstName());
        assertEquals(generatedUCC.getLastName(), createdUCC.getLastName());

        assert BCrypt.checkpw(generatedUCC.getPassword(), createdUCC.getPasswordHash());
    }

    @Test
    public void createUCC_verifyUCC_checkUCCDeletedAndUserPersistedCorrectly() throws Exception {
        UserCreationConfirmation ucc = App
                .generateAndSendUserCreationConfirmation(new UserCreationConfirmation(), this.mockMvc)
                .getFirst();

        // creating a second ucc with same email, both should be deleted when confirming the ucc
        UserCreationConfirmation uccWithSameEmail = new UserCreationConfirmation();
        uccWithSameEmail.setEmail(ucc.getEmail());
        ResultActions sameEmailCreateResult = App.generateAndSendUserCreationConfirmation(uccWithSameEmail, this.mockMvc).getSecond();

        sameEmailCreateResult.andExpect(status().isCreated());

        UserCreationConfirmation createdUcc = this.userCreationConfirmationRepository.findByUsernameIgnoreCase(ucc.getUsername()).get();
        String idAsString = createdUcc.getId().toString();

        App.verifyUserCreationConfirmation(idAsString, this.mockMvc)
                .andExpect(status().isOk());

        // checking that all the uccs were deleted with this email
        assertFalse(this.userCreationConfirmationRepository.existsByEmailIgnoreCase(ucc.getEmail()));

        Optional<User> userOpt = this.userRepository.findByUsernameIgnoreCase(ucc.getUsername());

        assert userOpt.isPresent();

        User user = userOpt.get();

        assertEquals(ucc.getEmail(), user.getEmail());
        assertEquals(ucc.getUsername(), user.getUsername());
        assertEquals(ucc.getFirstName(), user.getFirstName());
        assertEquals(ucc.getLastName(), user.getLastName());

        assert BCrypt.checkpw(ucc.getPassword(), user.getPasswordHash());
    }

    @Test
    public void attemptUsernameConflictWithExistingUCC() throws Exception {
        Pair<UserCreationConfirmation, ResultActions> uccRaPair = App.generateAndSendUserCreationConfirmation(new UserCreationConfirmation(),
                this.mockMvc);

        UserCreationConfirmation original = uccRaPair.getFirst();
        ResultActions originalCreateResult = uccRaPair.getSecond();

        originalCreateResult.andExpect(status().isCreated());

        UserCreationConfirmation conflict = new UserCreationConfirmation();
        conflict.setUsername(original.getUsername());

        ResultActions conflictResult = App.generateAndSendUserCreationConfirmation(conflict, this.mockMvc).getSecond();

        conflictResult.andExpect(status().isConflict());
    }

    @Test
    public void attemptUCCEmailConflictWithExistingUser() throws Exception {
        User existingUser = App.generateAndPersistUser(new User(), this.userRepository);

        UserCreationConfirmation conflict = new UserCreationConfirmation();
        conflict.setEmail(existingUser.getEmail());

        App.generateAndSendUserCreationConfirmation(conflict, this.mockMvc)
                .getSecond().andExpect(status().isConflict());
    }

    @Test
    public void attemptCreateUCCWithInvalidEmail() throws Exception {
        UserCreationConfirmation invalid = new UserCreationConfirmation();
        invalid.setEmail("invalid");

        MvcResult result = App.generateAndSendUserCreationConfirmation(invalid, this.mockMvc)
                .getSecond().andExpect(status().isUnprocessableEntity()).andReturn();

        Gson gson = new Gson();

        Map<String, List<String>> response =
                gson.fromJson(result.getResponse().getContentAsString(), Map.class);

        assert response.containsKey("messages");
        assertEquals(1, response.get("messages").size());
    }

    @Test
    public void attemptCreateUCCsWithInvalidUsername() throws Exception {
        Gson gson = new Gson();

        interface SendUCCWithInvalidUsernameFunction {
            void run(String invalidVal) throws Exception;
        }

        SendUCCWithInvalidUsernameFunction checkInvalidUsernameUCC = (invalidVal) -> {
            UserCreationConfirmation invalid = new UserCreationConfirmation();
            invalid.setUsername(invalidVal);

            MvcResult result = App.generateAndSendUserCreationConfirmation(invalid, this.mockMvc)
                    .getSecond().andReturn();

            Map<String, List<String>> response =
                    gson.fromJson(result.getResponse().getContentAsString(), Map.class);

            assert response.containsKey("messages");
            assertEquals(1, response.get("messages").size());
        };

        // username under the valid number of characters
        String tooShortVal = TestUtils.randStringOfLength(UserConstants.MIN_USERNAME_LENGTH - 1);
        checkInvalidUsernameUCC.run(tooShortVal);


        // username greater than the valid number of characters
        String tooLongVal = TestUtils.randStringOfLength(UserConstants.MAX_USERNAME_LENGTH + 1);
        checkInvalidUsernameUCC.run(tooLongVal);

        // username with space
        checkInvalidUsernameUCC.run("with space");
    }

    @Test
    public void attemptCreateUCCsWithInvalidFirstName() throws Exception {
        Gson gson = new Gson();

        interface SendUCCWithInvalidFirstNameFunction {
            void run(String invalidVal) throws Exception;
        }

        SendUCCWithInvalidFirstNameFunction checkInvalidFirstNameUCC = (invalidVal) -> {
            UserCreationConfirmation invalid = new UserCreationConfirmation();
            invalid.setFirstName(invalidVal);

            MvcResult result = App.generateAndSendUserCreationConfirmation(invalid, this.mockMvc)
                    .getSecond().andReturn();

            Map<String, List<String>> response =
                    gson.fromJson(result.getResponse().getContentAsString(), Map.class);

            assert response.containsKey("messages");
            assertEquals(1, response.get("messages").size());
        };

        // empty first name
        checkInvalidFirstNameUCC.run("");

        // too short first name
        String tooShortVal = TestUtils.randStringOfLength(UserConstants.MIN_FIRST_NAME_LENGTH - 1);
        checkInvalidFirstNameUCC.run(tooShortVal);

        // too long first name
        String tooLongVal = TestUtils.randStringOfLength(UserConstants.MAX_FIRST_NAME_LENGTH + 1);
        checkInvalidFirstNameUCC.run(tooLongVal);
    }

    @Test
    public void attemptCreateUCCsWithInvalidLastName() throws Exception {
        Gson gson = new Gson();

        // send too long last name
        String tooLongVal = TestUtils.randStringOfLength(UserConstants.MAX_LAST_NAME_LENGTH + 1);
        UserCreationConfirmation invalid = new UserCreationConfirmation();
        invalid.setLastName(tooLongVal);

        String jsonResp = App.generateAndSendUserCreationConfirmation(invalid, this.mockMvc)
                .getSecond()
                .andExpect(status().isUnprocessableEntity())
                .andReturn().getResponse().getContentAsString();

        Map<String, List<String>> response = gson.fromJson(jsonResp, Map.class);

        assert response.containsKey("messages");
        assertEquals(1, response.get("messages").size());
    }

    @Test
    public void attemptCreateUCCsWithInvalidPassword() throws Exception {
        Gson gson = new Gson();

        interface SendUCCWithInvalidPasswordFunction {
            void run(String invalidVal) throws Exception;
        }

        SendUCCWithInvalidPasswordFunction checkInvalidPasswordUCC = (invalidVal) -> {
            UserCreationConfirmation invalid = new UserCreationConfirmation();
            invalid.setPassword(invalidVal);

            String jsonResp = App.generateAndSendUserCreationConfirmation(invalid, this.mockMvc)
                    .getSecond().andExpect(status().isUnprocessableEntity())
                    .andReturn().getResponse().getContentAsString();

            Map<String, List<String>> response =gson.fromJson(jsonResp, Map.class);

            assert response.containsKey("messages");
            assertEquals(1, response.get("messages").size());
        };

        // password with no special chars
        String noSpecialChars = TestUtils.randStringOfLength(UserConstants.MIN_PASSWORD_LENGTH + 1);
        noSpecialChars += "1";
        checkInvalidPasswordUCC.run(noSpecialChars);

        // password with no numbers
        String noNumbers = TestUtils.randStringOfLength(UserConstants.MIN_PASSWORD_LENGTH + 1);
        noNumbers += "@";
        checkInvalidPasswordUCC.run(noNumbers);

        // password too short, but still contains all required char types
        String tooShort = "@a1";
        checkInvalidPasswordUCC.run(tooShort);

        // password too long
        String tooLong = TestUtils.randStringOfLength(UserConstants.MAX_PASSWORD_LENGTH);
        tooLong += "@1";
        checkInvalidPasswordUCC.run(tooLong);
    }

}
