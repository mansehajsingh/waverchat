package com.waverchat.api.integration.v1.organizationmember;

import com.google.gson.Gson;
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

import java.util.Map;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class OrganizationMemberResourceTest {

//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private OrganizationRepository organizationRepository;
//
//    @Autowired
//    private OrganizationMemberRepository organizationMemberRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Test
//    public void createCreateOrganizationMember_AndCheckPersistedCorrectly() throws Exception {
//        Pair<User, String> userTokenPair =
//                App.generatePersistUserAndSession(new User(), this.userRepository, this.mockMvc);
//
//        User user = userTokenPair.getFirst();
//        String accessToken = userTokenPair.getSecond();
//
//        Gson gson = new Gson();
//
//        Organization organization = App.generateOrganization(new Organization());
//        UUID orgId = UUID.fromString((String) gson.fromJson(App.createOrganization(organization, accessToken, this.mockMvc)
//                .andReturn().getResponse().getContentAsString(), Map.class).get("id"));
//
//        OrganizationMember orgMember = new OrganizationMember();
//    }

}
