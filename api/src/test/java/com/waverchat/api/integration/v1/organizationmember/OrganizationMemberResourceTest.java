package com.waverchat.api.integration.v1.organizationmember;

import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

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
