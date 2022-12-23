package com.waverchat.api.integration.v1.user;

import com.google.gson.Gson;
import com.mysema.commons.lang.Pair;
import com.waverchat.api.integration.v1.util.App;
import com.waverchat.api.integration.v1.util.TestUtils;
import com.waverchat.api.v1.resources.user.entity.User;
import com.waverchat.api.v1.resources.user.UserConstants;
import com.waverchat.api.v1.resources.user.UserRepository;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void checkGetResponse() throws Exception {
        User exampleUser = App.generateAndPersistUser(new User(), this.userRepository);

        Gson gson = new Gson();

        String idAsString = exampleUser.getId().toString();

        String jsonResp = App.fetchUserById(idAsString, this.mockMvc)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Map<String, String> response = gson.fromJson(jsonResp, Map.class);

        assert response.containsKey("id");
        assertEquals(exampleUser.getId().toString(), response.get("id"));

        assert response.containsKey("email");
        assertEquals(exampleUser.getEmail(), response.get("email"));

        assert response.containsKey("username");
        assertEquals(exampleUser.getUsername(), response.get("username"));

        assert response.containsKey("firstName");
        assertEquals(exampleUser.getFirstName(), response.get("firstName"));

        assert response.containsKey("lastName");
        assertEquals(exampleUser.getLastName(), response.get("lastName"));

        assert response.containsKey("createdAt");
        assert response.containsKey("updatedAt");
    }

    @Test
    public void checkPutResponseAndVerifyEditsPersistedCorrectly() throws Exception {
        Gson gson = new Gson();

        String newUsername = "edited", newFirstName = "EditedFirst", newLastName = "EditedLast";

        Pair<User, String> userAndAccessTokenPair= App.generatePersistUserAndSession(new User(), this.userRepository, this.mockMvc);

        User exampleUser = userAndAccessTokenPair.getFirst();
        String accessToken = userAndAccessTokenPair.getSecond();

        User editedUserRQ = new User();
        editedUserRQ.setUsername(newUsername);
        editedUserRQ.setFirstName(newFirstName);
        editedUserRQ.setLastName(newLastName);

        String idAsString = exampleUser.getId().toString();

        MvcResult result = App.editUserById(idAsString, editedUserRQ, accessToken, this.mockMvc)
                .andExpect(status().isOk()).andReturn();

        Map<String, String> response = gson.fromJson(result.getResponse().getContentAsString(), Map.class);

        assert response.containsKey("id");
        assertEquals(exampleUser.getId().toString(), response.get("id"));

        assert response.containsKey("email");
        assertEquals(exampleUser.getEmail(), response.get("email"));

        assert response.containsKey("username");
        assertEquals(newUsername, response.get("username"));

        assert response.containsKey("firstName");
        assertEquals(newFirstName, response.get("firstName"));

        assert response.containsKey("lastName");
        assertEquals(newLastName, response.get("lastName"));

        Optional<User> persistedUserOpt = this.userRepository.findById(exampleUser.getId());
        assert persistedUserOpt.isPresent();

        User persistedUser = persistedUserOpt.get();

        assertEquals(newUsername, persistedUser.getUsername());
        assertEquals(newFirstName, persistedUser.getFirstName());
        assertEquals(newLastName, persistedUser.getLastName());
    }

    @Test
    public void checkPutUsernameConflict() throws Exception {
        User existingFirst = App.generateAndPersistUser(new User(), this.userRepository);

        Pair<User, String> userTokenPair = App.generatePersistUserAndSession(new User(), this.userRepository, this.mockMvc);

        User existingSecond = userTokenPair.getFirst();
        String accessToken = userTokenPair.getSecond();

        User editDefaults = new User();
        editDefaults.setUsername(existingFirst.getUsername());

        App.editUserById(existingSecond.getId().toString(), editDefaults, accessToken, this.mockMvc)
                .andExpect(status().isConflict());
    }

    @Test
    public void checkPutInvalidUsername() throws Exception {
        Pair<User, String> userTokenPair = App.generatePersistUserAndSession(new User(), this.userRepository, this.mockMvc);

        User existingUser = userTokenPair.getFirst();
        String accessToken = userTokenPair.getSecond();

        String idAsString = existingUser.getId().toString();

        User tooShort = new User();
        tooShort.setUsername(TestUtils.randStringOfLength(UserConstants.MIN_USERNAME_LENGTH - 1));
        App.editUserById(idAsString, tooShort, accessToken, this.mockMvc).andExpect(status().isUnprocessableEntity());

        User tooLong = new User();
        tooLong.setUsername(TestUtils.randStringOfLength(UserConstants.MAX_USERNAME_LENGTH + 1));
        App.editUserById(idAsString, tooLong, accessToken, this.mockMvc).andExpect(status().isUnprocessableEntity());


        User withSpace = new User();
        withSpace.setUsername("with space");
        App.editUserById(idAsString, withSpace, accessToken, this.mockMvc).andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void checkPutInvalidFirstName() throws Exception {
        Pair<User, String> userTokenPair = App.generatePersistUserAndSession(new User(), this.userRepository, this.mockMvc);

        User existingUser = userTokenPair.getFirst();
        String accessToken = userTokenPair.getSecond();

        String idAsString = existingUser.getId().toString();

        User emptyName = new User();
        emptyName.setFirstName("");

        App.editUserById(idAsString, emptyName, accessToken, this.mockMvc);
    }

    private void checkQuery(MultiValueMap<String, String> params, User[] expected) throws Exception {
        Gson gson = new Gson();

        Map<String, List<Map<String, String>>> response = gson.fromJson(App.queryUsers(params, this.mockMvc)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(), Map.class);

        List<Map<String, String>> queriedUsers = response.get("entities");

        this.assertUserQueryIsCorrect(expected, queriedUsers);
    }

    private void assertUserQueryIsCorrect(User[] expected, List<Map<String, String>> actual) {
        assertEquals(expected.length, actual.size());

        Map<String, User> expectedById = new HashMap<>();

        for (User u : expected) {
            expectedById.put(u.getId().toString(), u);
        }

        for (Map<String, String> result : actual) {

            assert result.containsKey("id");
            assert expectedById.containsKey(result.get("id"));
            User match = expectedById.get(result.get("id"));

            assert result.containsKey("username");
            assertEquals(match.getUsername(), result.get("username"));

            assert result.containsKey("firstName");
            assertEquals(match.getFirstName(), result.get("firstName"));

            assert result.containsKey("lastName");
            assertEquals(match.getLastName(), result.get("lastName"));

            assert result.containsKey("createdAt");
            assert result.containsKey("updatedAt");

            expectedById.remove(result.get("id"));
        }

        assert expectedById.isEmpty();
    }

    @Test
    public void queryByEmail() throws Exception {
        User u1 = new User();
        u1.setEmail("user1@email.com");

        User u2 = new User();
        u2.setEmail("user2@example.com");

        User u3 = new User();
        u3.setEmail("not_user2@example.com");

        User u4 = new User();
        u4.setEmail("not_contains@none.com");

        App.generateAndPersistUser(u1, this.userRepository);
        App.generateAndPersistUser(u2, this.userRepository);
        App.generateAndPersistUser(u3, this.userRepository);
        App.generateAndPersistUser(u4, this.userRepository);

        // querying strict by username
        MultiValueMap<String, String> paramsStrict = new LinkedMultiValueMap<>();
        paramsStrict.add("email", u1.getEmail());
        this.checkQuery(paramsStrict, new User[] {u1});

        // querying with username starts with
        MultiValueMap<String, String> paramsStarts = new LinkedMultiValueMap<>();
        paramsStarts.add("email", "user*");
        this.checkQuery(paramsStarts, new User[] {u1, u2});

        // querying with username ends with
        MultiValueMap<String, String> paramsEnds = new LinkedMultiValueMap<>();
        paramsEnds.add("email", "*example.com");
        this.checkQuery(paramsEnds, new User[] {u2, u3});

        // querying with username contains
        MultiValueMap<String, String> paramsContains = new LinkedMultiValueMap<>();
        paramsContains.add("email", "*user*");
        this.checkQuery(paramsContains, new User[] {u1, u2, u3});
    }

    @Test
    public void queryByUsername() throws Exception {
        User u1 = new User();
        u1.setUsername("user1");

        User u2 = new User();
        u2.setUsername("user2");

        User u3 = new User();
        u3.setUsername("not_user2");

        User u4 = new User();
        u4.setUsername("not_contains");

        App.generateAndPersistUser(u1, this.userRepository);
        App.generateAndPersistUser(u2, this.userRepository);
        App.generateAndPersistUser(u3, this.userRepository);
        App.generateAndPersistUser(u4, this.userRepository);

        // querying strict by username
        MultiValueMap<String, String> paramsStrict = new LinkedMultiValueMap<>();
        paramsStrict.add("username", u1.getUsername());
        this.checkQuery(paramsStrict, new User[] {u1});

        // querying with username starts with
        MultiValueMap<String, String> paramsStarts = new LinkedMultiValueMap<>();
        paramsStarts.add("username", "user*");
        this.checkQuery(paramsStarts, new User[] {u1, u2});

        // querying with username ends with
        MultiValueMap<String, String> paramsEnds = new LinkedMultiValueMap<>();
        paramsEnds.add("username", "*user2");
        this.checkQuery(paramsEnds, new User[] {u2, u3});

        // querying with username contains
        MultiValueMap<String, String> paramsContains = new LinkedMultiValueMap<>();
        paramsContains.add("username", "*user*");
        this.checkQuery(paramsContains, new User[] {u1, u2, u3});
    }

    @Test
    public void queryByFirstName() throws Exception {
        User u1 = new User();
        u1.setFirstName("User1");

        User u2 = new User();
        u2.setFirstName("User2");

        User u3 = new User();
        u3.setFirstName("NOT USER2");

        User u4 = new User();
        u4.setUsername("NOT CONTAINS");

        App.generateAndPersistUser(u1, this.userRepository);
        App.generateAndPersistUser(u2, this.userRepository);
        App.generateAndPersistUser(u3, this.userRepository);
        App.generateAndPersistUser(u4, this.userRepository);

        // querying strict by first name
        MultiValueMap<String, String> paramsStrict = new LinkedMultiValueMap<>();
        paramsStrict.add("firstName", u1.getFirstName());
        this.checkQuery(paramsStrict, new User[] {u1});

        // querying with first name starts with
        MultiValueMap<String, String> paramsStarts = new LinkedMultiValueMap<>();
        paramsStarts.add("firstName", "user*");
        this.checkQuery(paramsStarts, new User[] {u1, u2});

        // querying with first name ends with
        MultiValueMap<String, String> paramsEnds = new LinkedMultiValueMap<>();
        paramsEnds.add("firstName", "*user2");
        this.checkQuery(paramsEnds, new User[] {u2, u3});

        // querying with first name contains
        MultiValueMap<String, String> paramsContains = new LinkedMultiValueMap<>();
        paramsContains.add("firstName", "*user*");
        this.checkQuery(paramsContains, new User[] {u1, u2, u3});
    }

    @Test
    public void queryByLastName() throws Exception {
        User u1 = new User();
        u1.setLastName("User1");

        User u2 = new User();
        u2.setLastName("User2");

        User u3 = new User();
        u3.setLastName("NOT USER2");

        User u4 = new User();
        u4.setLastName("NOT CONTAINS");

        App.generateAndPersistUser(u1, this.userRepository);
        App.generateAndPersistUser(u2, this.userRepository);
        App.generateAndPersistUser(u3, this.userRepository);
        App.generateAndPersistUser(u4, this.userRepository);

        // querying strict by last name
        MultiValueMap<String, String> paramsStrict = new LinkedMultiValueMap<>();
        paramsStrict.add("lastName", u1.getLastName());
        this.checkQuery(paramsStrict, new User[] {u1});

        // querying with last name starts with
        MultiValueMap<String, String> paramsStarts = new LinkedMultiValueMap<>();
        paramsStarts.add("lastName", "user*");
        this.checkQuery(paramsStarts, new User[] {u1, u2});

        // querying with last name ends with
        MultiValueMap<String, String> paramsEnds = new LinkedMultiValueMap<>();
        paramsEnds.add("lastName", "*user2");
        this.checkQuery(paramsEnds, new User[] {u2, u3});

        // querying with last name contains
        MultiValueMap<String, String> paramsContains = new LinkedMultiValueMap<>();
        paramsContains.add("lastName", "*user*");
        this.checkQuery(paramsContains, new User[] {u1, u2, u3});
    }

}
