package com.kafkamgt.uiapi.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafkamgt.uiapi.UtilMethods;
import com.kafkamgt.uiapi.dao.*;
import com.kafkamgt.uiapi.model.EnvModel;
import com.kafkamgt.uiapi.model.KafkaClustersType;
import com.kafkamgt.uiapi.model.TeamModel;
import com.kafkamgt.uiapi.model.UserInfoModel;
import com.kafkamgt.uiapi.service.EnvsClustersTenantsControllerService;
import com.kafkamgt.uiapi.service.UiConfigControllerService;
import com.kafkamgt.uiapi.service.UsersTeamsControllerService;
import java.util.HashMap;
import java.util.List;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UiConfigControllerTest {

  @MockBean private UiConfigControllerService uiConfigControllerService;

  @MockBean private EnvsClustersTenantsControllerService envsClustersTenantsControllerService;

  @MockBean private UsersTeamsControllerService usersTeamsControllerService;

  private UiConfigController uiConfigController;

  private UsersTeamsController usersTeamsController;

  private EnvsClustersTenantsController envsClustersTenantsController;

  private UtilMethods utilMethods;

  private MockMvc mvc, mvcUserTeams, mvcEnvs;

  @BeforeEach
  public void setUp() {
    utilMethods = new UtilMethods();

    uiConfigController = new UiConfigController();
    mvc = MockMvcBuilders.standaloneSetup(uiConfigController).dispatchOptions(true).build();
    ReflectionTestUtils.setField(
        uiConfigController, "uiConfigControllerService", uiConfigControllerService);

    usersTeamsController = new UsersTeamsController();
    mvcUserTeams =
        MockMvcBuilders.standaloneSetup(usersTeamsController).dispatchOptions(true).build();
    ReflectionTestUtils.setField(
        usersTeamsController, "usersTeamsControllerService", usersTeamsControllerService);

    envsClustersTenantsController = new EnvsClustersTenantsController();
    mvcEnvs =
        MockMvcBuilders.standaloneSetup(envsClustersTenantsController)
            .dispatchOptions(true)
            .build();
    ReflectionTestUtils.setField(
        envsClustersTenantsController,
        "envsClustersTenantsControllerService",
        envsClustersTenantsControllerService);
  }

  @Test
  @Order(1)
  public void getEnvs() throws Exception {
    List<EnvModel> envList = utilMethods.getEnvList();
    when(envsClustersTenantsControllerService.getKafkaEnvs()).thenReturn(envList);

    String res =
        mvcEnvs
            .perform(
                MockMvcRequestBuilders.get("/getEnvs")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    List<Env> response = new ObjectMapper().readValue(res, List.class);
    assertEquals(1, response.size());
  }

  @Test
  @Order(2)
  public void getSyncEnv() throws Exception {
    List<HashMap<String, String>> envList = utilMethods.getSyncEnv();
    when(envsClustersTenantsControllerService.getSyncEnvs()).thenReturn(envList);

    String res =
        mvcEnvs
            .perform(
                MockMvcRequestBuilders.get("/getSyncEnv")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    List<HashMap<String, String>> response = new ObjectMapper().readValue(res, List.class);
    assertEquals(2, response.size());
    assertEquals(2, response.get(0).size());
  }

  @Test
  @Order(4)
  public void getSchemaRegEnvs() throws Exception {
    List<EnvModel> envList = utilMethods.getEnvList();
    when(envsClustersTenantsControllerService.getSchemaRegEnvs()).thenReturn(envList);

    String res =
        mvcEnvs
            .perform(
                MockMvcRequestBuilders.get("/getSchemaRegEnvs")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    List<Env> response = new ObjectMapper().readValue(res, List.class);
    assertEquals(1, response.size());
  }

  @Test
  @Order(5)
  public void getSchemaRegEnvsStatus() throws Exception {
    List<EnvModel> envList = utilMethods.getEnvList();
    when(envsClustersTenantsControllerService.getSchemaRegEnvsStatus()).thenReturn(envList);

    String res =
        mvcEnvs
            .perform(
                MockMvcRequestBuilders.get("/getSchemaRegEnvsStatus")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    List<Env> response = new ObjectMapper().readValue(res, List.class);
    assertEquals(1, response.size());
  }

  @Test
  @Order(6)
  public void getAllTeamsSU() throws Exception {
    List<TeamModel> teamList = utilMethods.getTeamsModel();
    when(usersTeamsControllerService.getAllTeamsSU()).thenReturn(teamList);

    String res =
        mvcUserTeams
            .perform(
                MockMvcRequestBuilders.get("/getAllTeamsSU")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    List<Team> response = new ObjectMapper().readValue(res, List.class);
    assertEquals(1, response.size());
  }

  @Test
  @Order(7)
  public void getAllTeamsSUOnly() throws Exception {
    List<String> teamList = utilMethods.getAllTeamsSUOnly();
    when(usersTeamsControllerService.getAllTeamsSUOnly()).thenReturn(teamList);

    String res =
        mvcUserTeams
            .perform(
                MockMvcRequestBuilders.get("/getAllTeamsSUOnly")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    List<Team> response = new ObjectMapper().readValue(res, List.class);
    assertEquals(2, response.size());
  }

  @Test
  @Order(8)
  public void addNewEnv() throws Exception {
    EnvModel env = utilMethods.getEnvList().get(0);
    String jsonReq = new ObjectMapper().writer().writeValueAsString(env);
    when(envsClustersTenantsControllerService.addNewEnv(any())).thenReturn("success");

    String response =
        mvcEnvs
            .perform(
                MockMvcRequestBuilders.post("/addNewEnv")
                    .content(jsonReq)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    assertEquals("success", response);
  }

  @Test
  @Order(9)
  public void deleteEnv() throws Exception {
    HashMap<String, String> hashMap = new HashMap<>();
    hashMap.put("result", "success");
    when(envsClustersTenantsControllerService.deleteEnvironment(anyString(), anyString()))
        .thenReturn(hashMap);

    String response =
        mvcEnvs
            .perform(
                MockMvcRequestBuilders.post("/deleteEnvironmentRequest")
                    .param("envId", "101")
                    .param("envType", KafkaClustersType.KAFKA.value)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    assertThat(response, CoreMatchers.containsString("success"));
  }

  @Test
  @Order(10)
  public void deleteTeam() throws Exception {
    when(usersTeamsControllerService.deleteTeam(any())).thenReturn("success");

    String response =
        mvcUserTeams
            .perform(
                MockMvcRequestBuilders.post("/deleteTeamRequest")
                    .param("teamId", "101")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    assertEquals("success", response);
  }

  @Test
  @Order(11)
  public void deleteUser() throws Exception {
    when(usersTeamsControllerService.deleteUser(anyString(), anyBoolean())).thenReturn("success");

    String response =
        mvcUserTeams
            .perform(
                MockMvcRequestBuilders.post("/deleteUserRequest")
                    .param("userId", "uiuser1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    assertEquals("success", response);
  }

  @Test
  @Order(12)
  public void addNewUser() throws Exception {
    HashMap<String, String> result = new HashMap<>();
    result.put("result", "success");
    UserInfoModel userInfo = utilMethods.getUserInfoMock();
    String jsonReq = new ObjectMapper().writer().writeValueAsString(userInfo);
    when(usersTeamsControllerService.addNewUser(any(), anyBoolean())).thenReturn(result);

    String response =
        mvcUserTeams
            .perform(
                MockMvcRequestBuilders.post("/addNewUser")
                    .content(jsonReq)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    assertThat(response, CoreMatchers.containsString("success"));
  }

  @Test
  @Order(13)
  public void addNewTeam() throws Exception {
    Team team = utilMethods.getTeams().get(0);
    String jsonReq = new ObjectMapper().writer().writeValueAsString(team);
    String result = "{ \"status\": \"" + "success" + "\" }";
    when(usersTeamsControllerService.addNewTeam(any(), anyBoolean())).thenReturn(result);

    String response =
        mvcUserTeams
            .perform(
                MockMvcRequestBuilders.post("/addNewTeam")
                    .content(jsonReq)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    assertThat(response, CoreMatchers.containsString("success"));
  }

  @Test
  @Order(14)
  public void changePwd() throws Exception {
    when(usersTeamsControllerService.changePwd(any())).thenReturn("success");

    String response =
        mvcUserTeams
            .perform(
                MockMvcRequestBuilders.post("/chPwd")
                    .param("changePwd", "newpasswd")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    assertEquals("success", response);
  }

  @Test
  @Order(15)
  public void showUsers() throws Exception {
    List<UserInfoModel> userList = utilMethods.getUserInfoListModel("uiuser", "ADMIN");
    when(usersTeamsControllerService.showUsers(any(), any(), any())).thenReturn(userList);

    String res =
        mvcUserTeams
            .perform(
                MockMvcRequestBuilders.get("/showUserList")
                    .param("teamName", "")
                    .param("pageNo", "1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    List<UserInfo> response = new ObjectMapper().readValue(res, List.class);
    assertEquals(1, response.size());
  }

  @Test
  @Order(16)
  public void getMyProfileInfo() throws Exception {
    UserInfoModel userInfo = utilMethods.getUserInfoMock();
    when(usersTeamsControllerService.getMyProfileInfo()).thenReturn(userInfo);

    String res =
        mvcUserTeams
            .perform(
                MockMvcRequestBuilders.get("/getMyProfileInfo")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    UserInfoModel response = new ObjectMapper().readValue(res, UserInfoModel.class);
    assertEquals("Seahorses", response.getTeam());
  }

  @Test
  @Order(17)
  public void showActivityLog() throws Exception {
    List<ActivityLog> activityLogs = utilMethods.getLogs();
    when(uiConfigControllerService.showActivityLog(anyString(), anyString(), anyString()))
        .thenReturn(activityLogs);

    String res =
        mvc.perform(
                MockMvcRequestBuilders.get("/getActivityLogPerEnv")
                    .param("env", "1")
                    .param("pageNo", "1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    List<ActivityLog> response = new ObjectMapper().readValue(res, List.class);
    assertEquals(1, response.size());
  }
}
