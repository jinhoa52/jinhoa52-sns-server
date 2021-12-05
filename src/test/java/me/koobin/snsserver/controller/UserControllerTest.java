package me.koobin.snsserver.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.koobin.snsserver.exception.InValidValueException;
import me.koobin.snsserver.model.User;
import me.koobin.snsserver.model.UserIdAndPassword;
import me.koobin.snsserver.model.UserPasswordUpdateParam;
import me.koobin.snsserver.model.UserSignUpParam;
import me.koobin.snsserver.model.UserUpdateParam;
import me.koobin.snsserver.service.FileIOService;
import me.koobin.snsserver.service.LoginService;
import me.koobin.snsserver.service.UserService;
import me.koobin.snsserver.util.PasswordEncryptor;
import me.koobin.snsserver.util.SessionKey;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(UserController.class)
class UserControllerTest {

  @Autowired
  MockMvc mockMvc;

  static MockHttpSession mockHttpSession;

  @MockBean
  UserService userService;

  @MockBean
  FileIOService fileIOService;

  @MockBean
  LoginService loginService;

  final String baseUrl = "/users";

  static ObjectMapper mapper;

  User testUser;

  User encryptedTestUser;

  @BeforeAll
  static void initAll() {
    mapper = new ObjectMapper();
    mockHttpSession = new MockHttpSession();
  }

  @BeforeEach
  void initEach() {
    testUser = User.builder()
        .username("id")
        .password("password")
        .email("email@email.com")
        .phoneNumber("010-0000-0000")
        .name("test1")
        .id(1L)
        .build();

    encryptedTestUser = User.builder()
        .username("id")
        .password(PasswordEncryptor.encrypt("password"))
        .email("email@email.com")
        .phoneNumber("010-0000-0000")
        .name("test1")
        .id(1L)
        .build();

    mockHttpSession.clearAttributes();
  }

  @Test
  void signup_success() throws Exception {
    when(userService.signUp(any(UserSignUpParam.class))).thenReturn(true);

    mockMvc.perform(post(baseUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(testUser))
        ).andDo(print())
        .andExpect(status().isCreated());
  }

  @Test
  void signup_fail_dupeUsername() throws Exception {
    when(userService.signUp(any(UserSignUpParam.class))).thenReturn(false);

    mockMvc.perform(post(baseUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(testUser))
        ).andDo(print())
        .andExpect(status().isConflict());
  }

  @Test
  void checkUsernameDupe_Dupe() throws Exception {

    when(userService.isUsernameDupe(testUser.getUsername())).thenReturn(true);

    mockMvc.perform(get(baseUrl + '/' + testUser.getUsername() + "/exists")
        ).andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string("true"));

    verify(userService).isUsernameDupe(testUser.getUsername());
  }

  @Test
  void checkUsernameDupe_NotDupe() throws Exception {

    when(userService.isUsernameDupe(testUser.getUsername())).thenReturn(false);

    mockMvc.perform(get(baseUrl + '/' + testUser.getUsername() + "/exists")
        ).andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string("false"));

    verify(userService).isUsernameDupe(testUser.getUsername());
  }

  @Test
  void login() throws Exception {
    UserIdAndPassword userIdAndPassword =
        new UserIdAndPassword(testUser.getUsername(), testUser.getPassword());

    when(userService.getLoginUser(any(UserIdAndPassword.class))).thenReturn(encryptedTestUser);

    mockMvc.perform(post(baseUrl + "/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(userIdAndPassword))
        ).andDo(print())
        .andExpect(status().isOk());

    verify(userService).getLoginUser(any(UserIdAndPassword.class));
    verify(loginService).loginUser(encryptedTestUser);
  }

  @Test
  void logout() throws Exception {
    mockMvc.perform(get(baseUrl + "/logout")
            .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print())
        .andExpect(status().isOk());
    verify(loginService).logoutUser();
  }

  @Test
  void updateUser() throws Exception {
    mockHttpSession.setAttribute(SessionKey.USER, encryptedTestUser);
    MockMultipartFile mockFile = new MockMultipartFile(
        "profileImage",
        "profileImage",
        "image/png",
        "profileImage".getBytes());
    UserUpdateParam userUpdateParam =
        new UserUpdateParam("updateName", "010-2222-2222", "update@email.com", "message");

    MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart(
        "/users/profile");
    builder.with(request -> {
      request.setMethod("PUT");
      return request;
    });

    mockMvc.perform(builder
            .file(mockFile)
            .session(mockHttpSession)
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .content(mapper.writeValueAsString(userUpdateParam)))
        .andDo(print())
        .andExpect(status().isOk());

    verify(userService).updateUser(eq(encryptedTestUser), any(UserUpdateParam.class), eq(mockFile));
  }

  @Test
  void updateUserPassword_success() throws Exception {
    mockHttpSession.setAttribute(SessionKey.USER, encryptedTestUser);
    UserPasswordUpdateParam userPasswordUpdateParam
        = new UserPasswordUpdateParam(encryptedTestUser.getPassword(), "update", "update");

    mockMvc.perform(put(baseUrl + "/profile/password")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(userPasswordUpdateParam))
            .session(mockHttpSession)
        ).andDo(print())
        .andExpect(status().isOk());

    verify(userService).updateUserPassword(any(User.class), any(UserPasswordUpdateParam.class));
    verify(loginService).logoutUser();
  }

  @Test
  void updateUserPassword_fail_throwInValidValue() throws Exception {
    mockHttpSession.setAttribute(SessionKey.USER, encryptedTestUser);
    UserPasswordUpdateParam userPasswordUpdateParam
        = new UserPasswordUpdateParam(encryptedTestUser.getPassword(), "new", "check");

    doThrow(InValidValueException.class)
        .when(userService)
        .updateUserPassword(any(User.class), any(UserPasswordUpdateParam.class));

    mockMvc.perform(put(baseUrl + "/profile/password")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(userPasswordUpdateParam))
            .session(mockHttpSession)
        ).andDo(print())
        .andExpect(status().isConflict());

    verify(userService).updateUserPassword(any(User.class), any(UserPasswordUpdateParam.class));
    verify(loginService, never()).logoutUser();
  }

  @Test
  void deleteUser() throws Exception {
    mockHttpSession.setAttribute(SessionKey.USER, encryptedTestUser);

    mockMvc.perform(delete(baseUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(testUser.getPassword()))
            .session(mockHttpSession)
        ).andDo(print())
        .andExpect(status().isOk());
    verify(userService).deleteUser(any(User.class), any(String.class));
    verify(loginService).logoutUser();
  }
}