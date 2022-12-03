package com.waverchat.api.integration.v1.util;

import com.google.gson.Gson;
import com.mysema.commons.lang.Pair;
import com.waverchat.api.v1.resources.organization.Organization;
import com.waverchat.api.v1.resources.user.User;
import com.waverchat.api.v1.resources.user.UserRepository;
import com.waverchat.api.v1.resources.usercreationconfirmation.UserCreationConfirmation;
import org.aspectj.weaver.ast.Or;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class App {

    public static ResultActions verifyUserCreationConfirmation(String id, MockMvc mockMvc) throws Exception {
        return mockMvc.perform(
                get("/api/v1/user-creation-confirmations/" + id));
    }


    public static Pair<UserCreationConfirmation, ResultActions> generateAndSendUserCreationConfirmation(
            UserCreationConfirmation defaults, MockMvc mockMvc
    )
            throws Exception
    {
        if (defaults.getEmail() == null) {
            defaults.setEmail(TestUtils.randStringOfLength(8) + "@email.com");
        }

        if (defaults.getUsername() == null) {
            defaults.setUsername(TestUtils.randStringOfLength(8));
        }

        if (defaults.getFirstName() == null) {
            defaults.setFirstName(TestUtils.randStringOfLength(8));
        }

        if (defaults.getLastName() == null) {
            defaults.setLastName(TestUtils.randStringOfLength(8));
        }

        if (defaults.getPassword() == null) {
            defaults.setPassword("@" + TestUtils.randStringOfLength(8) + "123");
        }

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", defaults.getEmail());
        requestBody.put("username", defaults.getUsername());
        requestBody.put("firstName", defaults.getFirstName());
        requestBody.put("lastName", defaults.getLastName());
        requestBody.put("password", defaults.getPassword());

        Gson gson = new Gson();

        String json = gson.toJson(requestBody);

        ResultActions result = mockMvc.perform(
            post("/api/v1/user-creation-confirmations")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json));

        return new Pair<>(defaults, result);
    }

    public static User generateAndPersistUser(User defaults, UserRepository repo) {

        if (defaults.getEmail() == null) {
            defaults.setEmail(TestUtils.randStringOfLength(8) + "@email.com");
        }

        if (defaults.getUsername() == null) {
            defaults.setUsername(TestUtils.randStringOfLength(8));
        }

        if (defaults.getFirstName() == null) {
            defaults.setFirstName(TestUtils.randStringOfLength(8));
        }

        if (defaults.getLastName() == null) {
            defaults.setLastName(TestUtils.randStringOfLength(8));
        }

        if (defaults.getPassword() == null) {
            defaults.setPassword("@" + TestUtils.randStringOfLength(8) + "123");
        }

        String hashedPassword = BCrypt.hashpw(defaults.getPassword(), BCrypt.gensalt());
        defaults.setPasswordHash(hashedPassword);

        defaults.setDeleted(false);
        defaults.setSuperUser(false);

        User createdUser = repo.save(defaults);
        createdUser.setPassword(defaults.getPassword());

        return createdUser;
    }

    public static ResultActions fetchUserById(String id, MockMvc mockMvc) throws Exception {
        return mockMvc.perform(get("/api/v1/users/" + id));
    }

    public static Pair<User, String> generatePersistUserAndSession(
            User defaults, UserRepository userRepository, MockMvc mockMvc
    ) throws Exception {
        User createdUser = generateAndPersistUser(defaults, userRepository);

        Map<String, String> sessionCreationRequest = new HashMap<>();
        sessionCreationRequest.put("email", createdUser.getEmail());
        sessionCreationRequest.put("password", createdUser.getPassword());

        Gson gson = new Gson();

        String jsonBody = gson.toJson(sessionCreationRequest);

        String jsonResp = mockMvc.perform(
                post("/api/v1/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andReturn().getResponse().getContentAsString();

        Map<String, String> response = gson.fromJson(jsonResp, Map.class);

        return new Pair<>(createdUser, response.get("accessToken"));
    }
    public static ResultActions editUserById(String id, User defaults, String accessToken, MockMvc mockMvc) throws Exception {
        Map<String, String> requestBody = new HashMap<>();

        if (defaults.getUsername() != null)
            requestBody.put("username", defaults.getUsername());

        if (defaults.getFirstName() != null)
            requestBody.put("firstName", defaults.getFirstName());

        if (defaults.getLastName() != null)
            requestBody.put("lastName", defaults.getLastName());

        Gson gson = new Gson();

        String jsonBody = gson.toJson(requestBody);

        return mockMvc.perform(put("/api/v1/users/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody)
                .header("authorization", "Bearer " + accessToken));
    }

    public static ResultActions queryUsers(MultiValueMap<String, String> queryParams, MockMvc mockMvc) throws Exception {
        return mockMvc.perform(
                get("/api/v1/users")
                        .params(queryParams));
    }

    public static Organization generateOrganization(Organization defaults) {
        Organization org = new Organization();

        if (defaults.getName() != null)
            org.setName(defaults.getName());
        else
            org.setName(TestUtils.randStringOfLength(8));

        if (defaults.getDescription() != null)
            org.setDescription(defaults.getDescription());
        else
            org.setDescription(TestUtils.randStringOfLength(40));

        return org;
    }

    public static ResultActions createOrganization(Organization org, String accessToken, MockMvc mockMvc)
            throws Exception
    {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", org.getName());
        requestBody.put("description", org.getDescription());

        Gson gson = new Gson();

        String jsonBody = gson.toJson(requestBody);

        return mockMvc.perform(post("/api/v1/organizations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody)
                .header("authorization", "Bearer " + accessToken));
    }

    public static ResultActions fetchOrganization(UUID id, String accessToken, MockMvc mockMvc) throws Exception {
        return mockMvc.perform(get("/api/v1/organizations/" + id.toString())
                .header("authorization", "Bearer " + accessToken));
    }

}
