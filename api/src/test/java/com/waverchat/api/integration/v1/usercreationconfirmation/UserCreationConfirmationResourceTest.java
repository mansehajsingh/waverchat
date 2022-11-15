package com.waverchat.api.integration.v1.usercreationconfirmation;

import com.google.gson.Gson;
import com.waverchat.api.v1.applicationresource.user.User;
import com.waverchat.api.v1.applicationresource.user.UserConstants;
import com.waverchat.api.v1.applicationresource.user.UserRepository;
import com.waverchat.api.v1.applicationresource.usercreationconfirmation.UserCreationConfirmation;
import com.waverchat.api.v1.applicationresource.usercreationconfirmation.UserCreationConfirmationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
public class UserCreationConfirmationResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserCreationConfirmationRepository userCreationConfirmationRepository;

    @Autowired
    private UserRepository userRepository;

    protected static boolean createdExampleUCC = false;

    protected static boolean createdExampleUser = false;

    protected static final String CREATED_UCC_EMAIL = "example@email.com";
    protected static final String CREATED_UCC_USERNAME = "example";
    protected static final String CREATED_UCC_FIRST_NAME = "Example";
    protected static final String CREATED_UCC_LAST_NAME = "";
    protected static final String CREATED_UCC_PASSWORD = "@password123";

    protected static final String CREATED_USER_EMAIL = "existinguser@email.com";
    protected static final String CREATED_USER_USERNAME = "existing_user";
    protected static final String CREATED_USER_FIRST_NAME = "Existing";
    protected static final String CREATED_USER_LAST_NAME = "User";
    protected static final String CREATED_USER_PASSWORD = "@password123";

    private Map<String, Object> produceUCCCreateRequestBody(String email, String username, String firstName, String lastName, String password) {
        Map<String, Object> requestBody = new HashMap<>();

        requestBody.put("email", email);
        requestBody.put("username", username);
        requestBody.put("firstName", firstName);
        requestBody.put("lastName", lastName);
        requestBody.put("password", password);

        return requestBody;
    }

    private ResultActions sendUCC(Map<String, Object> requestBody) throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(requestBody);
        return this.mockMvc.perform(
                post("/api/v1/user-creation-confirmations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json));
    }

    private String produceStringOfLength(int length, char c) {
        char[] chars = new char[length];
        Arrays.fill(chars, c);
        return new String(chars);
    }

    @Before
    public void createExampleUserCreationConfirmation() {

        if (createdExampleUCC) return;

        String hashedPassword = BCrypt.hashpw(CREATED_UCC_PASSWORD, BCrypt.gensalt());

        UserCreationConfirmation userCreationConfirmation = new UserCreationConfirmation(
                CREATED_UCC_EMAIL,
                CREATED_UCC_USERNAME,
                hashedPassword,
                "",
                CREATED_UCC_FIRST_NAME,
                CREATED_UCC_LAST_NAME,
                false,
                false
        );

        this.userCreationConfirmationRepository.save(userCreationConfirmation);

        createdExampleUCC = true;
    }

    @Before
    public void createExampleUser() {
        if (createdExampleUser) return;

        String hashedPassword = BCrypt.hashpw(CREATED_USER_PASSWORD, BCrypt.gensalt());

        User user = new User(
                CREATED_USER_EMAIL,
                CREATED_USER_USERNAME,
                hashedPassword,
                CREATED_USER_FIRST_NAME,
                CREATED_USER_LAST_NAME,
                false,
                false
        );

        this.userRepository.save(user);

        createdExampleUser = true;
    }

    @Test
    public void checkCreateResponse() throws Exception {
        Map<String, Object> requestBody = this
                .produceUCCCreateRequestBody("user@email.com", "username", "First", "Last", "@password123");

        MvcResult creationResult = this.sendUCC(requestBody)
                .andExpect(status().isCreated())
                .andReturn();

        Gson gson = new Gson();

        Map<String, String> response =
                gson.fromJson(creationResult.getResponse().getContentAsString(), Map.class);


        assert response.containsKey("email");
        assertEquals(requestBody.get("email"), response.get("email"));

        assert response.containsKey("username");
        assertEquals(requestBody.get("username"), response.get("username"));

        assert response.containsKey("firstName");
        assertEquals(requestBody.get("firstName"), response.get("firstName"));

        assert response.containsKey("lastName");
        assertEquals(requestBody.get("lastName"), response.get("lastName"));
    }

    @Test
    public void checkUserCreationConfirmationPersistedCorrectly() {
        Optional<UserCreationConfirmation> uccOpt = this.userCreationConfirmationRepository.findByUsernameIgnoreCase("example");

        assert uccOpt.isPresent();

        UserCreationConfirmation createdUCC = uccOpt.get();

        assertEquals(CREATED_UCC_EMAIL, createdUCC.getEmail());
        assertEquals(CREATED_UCC_USERNAME, createdUCC.getUsername());
        assertEquals(CREATED_UCC_FIRST_NAME, createdUCC.getFirstName());
        assertEquals(CREATED_UCC_LAST_NAME, createdUCC.getLastName());

        assert BCrypt.checkpw(CREATED_UCC_PASSWORD, createdUCC.getPasswordHash());
    }

    @Test
    public void createUCC_verifyUCC_checkUCCDeletedAndUserPersistedCorrectly() throws Exception {
        String email = "verified@email.com", username = "verified", firstName = "Verified", lastName = "", password = "@password123";
        Map<String, Object> requestBody = this.produceUCCCreateRequestBody(
                email, username, firstName, lastName, password
        );
        this.sendUCC(requestBody);

        // creating a second ucc with same email, both should be deleted when confirming the ucc
        Map<String, Object> requestBodyWithSameEmail = this.produceUCCCreateRequestBody(
                email, "verified2", firstName, lastName, password
        );
        this.sendUCC(requestBodyWithSameEmail);

        UserCreationConfirmation createdUcc = this.userCreationConfirmationRepository.findByUsernameIgnoreCase(username).get();
        String idAsString = createdUcc.getId().toString();
        this.mockMvc.perform(
                get("/api/v1/user-creation-confirmations/" + idAsString))
                .andExpect(status().isOk());

        // checking that all the uccs were deleted with this email
        assertFalse(this.userCreationConfirmationRepository.existsByEmailIgnoreCase(email));

        Optional<User> userOpt = this.userRepository.findByUsernameIgnoreCase(username);

        assert userOpt.isPresent();

        User user = userOpt.get();

        assertEquals(email, user.getEmail());
        assertEquals(username, user.getUsername());
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());

        assert BCrypt.checkpw(password, user.getPasswordHash());
    }

    @Test
    public void attemptUsernameConflictWithExistingUCC() throws Exception {
        Map<String, Object> requestBody = this.produceUCCCreateRequestBody(
                "failed@email.com", CREATED_UCC_USERNAME, "failed", "failed", "@password123");

        this.sendUCC(requestBody).andExpect(status().isConflict());
    }

    @Test
    public void attemptUserNameConflictWithExistingUser() throws Exception {
        Map<String, Object> requestBody = this.produceUCCCreateRequestBody(
                "failed@email.com", CREATED_USER_USERNAME, "failed", "failed", "@password123"
        );

        this.sendUCC(requestBody).andExpect(status().isConflict());
    }

    @Test
    public void attemptEmailConflictWithExistingUser() throws Exception {
        Map<String, Object> requestBody = this.produceUCCCreateRequestBody(
                CREATED_USER_EMAIL, "failed", "failed", "failed", "@password123"
        );

        this.sendUCC(requestBody).andExpect(status().isConflict());
    }

    @Test
    public void attemptCreateUCCWithInvalidEmail() throws Exception {
        Map<String, Object> requestBody = this.produceUCCCreateRequestBody(
                "invalid", "failed", "failed", "failed", "@password123"
        );

        MvcResult result = this.sendUCC(requestBody)
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

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
            Map<String, Object> requestBody = this.produceUCCCreateRequestBody(
                    "failed@email.com", invalidVal, "failed", "failed", "@password123");
            MvcResult result = this.sendUCC(requestBody)
                    .andExpect(status().isUnprocessableEntity()).andReturn();
            Map<String, List<String>> response =
                    gson.fromJson(result.getResponse().getContentAsString(), Map.class);

            assert response.containsKey("messages");
            assertEquals(1, response.get("messages").size());
        };

        // username under the valid number of characters
        String tooShortVal = this.produceStringOfLength(UserConstants.MIN_USERNAME_LENGTH - 1, 'a');
        checkInvalidUsernameUCC.run(tooShortVal);


        // username greater than the valid number of characters
        String tooLongVal = this.produceStringOfLength(UserConstants.MAX_USERNAME_LENGTH + 1, 'a');
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
            Map<String, Object> requestBody = this.produceUCCCreateRequestBody(
                    "failed@email.com", "failed", invalidVal, "failed", "@password123");
            MvcResult result = this.sendUCC(requestBody)
                    .andExpect(status().isUnprocessableEntity()).andReturn();
            Map<String, List<String>> response =
                    gson.fromJson(result.getResponse().getContentAsString(), Map.class);

            assert response.containsKey("messages");
            assertEquals(1, response.get("messages").size());
        };

        // empty first name
        checkInvalidFirstNameUCC.run("");

        // too short first name
        String tooShortVal = this.produceStringOfLength(UserConstants.MIN_FIRST_NAME_LENGTH - 1, 'a');
        checkInvalidFirstNameUCC.run(tooShortVal);

        // too long first name
        String tooLongVal = this.produceStringOfLength(UserConstants.MAX_FIRST_NAME_LENGTH + 1, 'a');
        checkInvalidFirstNameUCC.run(tooLongVal);
    }

    @Test
    public void attemptCreateUCCsWithInvalidLastName() throws Exception {
        Gson gson = new Gson();

        // send too long last name
        String tooLongVal = this.produceStringOfLength(UserConstants.MAX_LAST_NAME_LENGTH + 1, 'a');
        Map<String, Object> requestBody = this.produceUCCCreateRequestBody(
                "failed@email.com", "failed", "failed", tooLongVal, "@password123");

        Map<String, List<String>> response = gson.fromJson(this.sendUCC(requestBody).andExpect(status().isUnprocessableEntity())
                .andReturn().getResponse().getContentAsString(), Map.class);

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
            Map<String, Object> requestBody = this.produceUCCCreateRequestBody(
                    "failed@email.com", "failed", "failed", "failed", invalidVal);
            MvcResult result = this.sendUCC(requestBody)
                    .andExpect(status().isUnprocessableEntity()).andReturn();
            Map<String, List<String>> response =
                    gson.fromJson(result.getResponse().getContentAsString(), Map.class);

            assert response.containsKey("messages");
            assertEquals(1, response.get("messages").size());
        };

        // password with no special chars
        String noSpecialChars = this.produceStringOfLength(UserConstants.MIN_PASSWORD_LENGTH + 1, 'a');
        noSpecialChars += "1";
        checkInvalidPasswordUCC.run(noSpecialChars);

        // password with no numbers
        String noNumbers = this.produceStringOfLength(UserConstants.MIN_PASSWORD_LENGTH + 1, 'a');
        noNumbers += "@";
        checkInvalidPasswordUCC.run(noNumbers);

        // password too short
        String tooShort = "@a1";
        checkInvalidPasswordUCC.run(tooShort);

        // password too long
        String tooLong = this.produceStringOfLength(UserConstants.MAX_PASSWORD_LENGTH, 'a');
        tooLong += "@1";
        checkInvalidPasswordUCC.run(tooLong);
    }

}
