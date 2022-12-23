package com.waverchat.api.integration.v1.organization;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.mysema.commons.lang.Pair;
import com.waverchat.api.integration.v1.util.App;
import com.waverchat.api.v1.resources.organization.Organization;
import com.waverchat.api.v1.resources.organization.OrganizationRepository;
import com.waverchat.api.v1.resources.organizationmember.OrganizationMember;
import com.waverchat.api.v1.resources.organizationmember.OrganizationMemberRepository;
import com.waverchat.api.v1.resources.organizationmember.OrganizationMembershipType;
import com.waverchat.api.v1.resources.user.User;
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
import org.springframework.test.web.servlet.ResultActions;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class OrganizationResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private OrganizationMemberRepository organizationMemberRepository;

    @Test
    public void checkOrganizationCreationResponse_andPersistedCorrectly() throws Exception {
        Pair<User, String> userTokenPair =
                App.generatePersistUserAndSession(new User(), this.userRepository, this.mockMvc);

        User user = userTokenPair.getFirst();

        String accessToken = userTokenPair.getSecond();

        Organization generatedOrg = App.generateOrganization(new Organization());

        ResultActions createResult = App.createOrganization(generatedOrg, accessToken, this.mockMvc);

        createResult.andExpect(status().isCreated());

        Gson gson = new Gson();

        Map<String, Object> response = gson.fromJson(
                createResult.andReturn().getResponse().getContentAsString(), Map.class);

        assert response.containsKey("id");

        assert response.containsKey("name");
        assertEquals(generatedOrg.getName(), response.get("name"));

        assert response.containsKey("description");
        assertEquals(generatedOrg.getDescription(), response.get("description"));

        assert response.containsKey("createdAt");
        assert response.containsKey("updatedAt");

        Map<String, String> ownerResponse = (LinkedTreeMap<String, String>) response.get("owner");

        assert ownerResponse.containsKey("id");
        assertEquals(user.getId().toString(), ownerResponse.get("id"));

        assert ownerResponse.containsKey("username");
        assertEquals(user.getUsername(), ownerResponse.get("username"));

        assert ownerResponse.containsKey("email");
        assertEquals(user.getEmail(), ownerResponse.get("email"));

        assert ownerResponse.containsKey("firstName");
        assertEquals(user.getFirstName(), ownerResponse.get("firstName"));

        assert ownerResponse.containsKey("lastName");
        assertEquals(user.getLastName(), ownerResponse.get("lastName"));

        assert ownerResponse.containsKey("createdAt");
        assert ownerResponse.containsKey("updatedAt");

        Optional<Organization> persistedOrgOpt = this.organizationRepository.findById(
                Long.parseLong((String) response.get("id"))
        );

        assert persistedOrgOpt.isPresent();
        Organization persistedOrg = persistedOrgOpt.get();

        assertEquals(generatedOrg.getName(), persistedOrg.getName());
        assertEquals(generatedOrg.getDescription(), persistedOrg.getDescription());

        Optional<OrganizationMember> persistedOwnerOpt = this.organizationMemberRepository
                        .findByOrganization_Id(Long.parseLong((String) response.get("id")));

        assert persistedOwnerOpt.isPresent();
        OrganizationMember persistedOwner = persistedOwnerOpt.get();

        assertEquals(OrganizationMembershipType.OWNER, persistedOwner.getType());
        assertEquals(user.getId(), persistedOwner.getMember().getId());
    }

    @Test
    public void checkOrganizationViewResponse() throws Exception {
        Pair<User, String> userTokenPair = App.generatePersistUserAndSession(new User(), this.userRepository, this.mockMvc);
        User user = userTokenPair.getFirst();
        String accessToken = userTokenPair.getSecond();

        Gson gson = new Gson();

        Organization organization = App.generateOrganization(new Organization());
        UUID orgId = UUID.fromString((String) gson.fromJson(App.createOrganization(organization, accessToken, this.mockMvc)
                .andReturn().getResponse().getContentAsString(), Map.class).get("id"));

        ResultActions viewResult = App.fetchOrganization(orgId, accessToken, this.mockMvc);

        Map<String, Object> response = gson.fromJson(
                viewResult.andReturn().getResponse().getContentAsString(), Map.class);

        assert response.containsKey("id");
        assertEquals(orgId.toString(), response.get("id"));

        assert response.containsKey("name");
        assertEquals(organization.getName(), response.get("name"));

        assert response.containsKey("description");
        assertEquals(organization.getDescription(), response.get("description"));

        assert response.containsKey("createdAt");
        assert response.containsKey("updatedAt");

        Map<String, String> ownerResponse = (LinkedTreeMap<String, String>) response.get("owner");

        assert ownerResponse.containsKey("id");
        assertEquals(user.getId().toString(), ownerResponse.get("id"));

        assert ownerResponse.containsKey("email");
        assertEquals(user.getEmail(), ownerResponse.get("email"));

        assert ownerResponse.containsKey("username");
        assertEquals(user.getUsername(), ownerResponse.get("username"));

        assert ownerResponse.containsKey("firstName");
        assertEquals(user.getFirstName(), ownerResponse.get("firstName"));

        assert ownerResponse.containsKey("lastName");
        assertEquals(user.getLastName(), ownerResponse.get("lastName"));

        assert ownerResponse.containsKey("createdAt");
        assert ownerResponse.containsKey("updatedAt");
    }

}
