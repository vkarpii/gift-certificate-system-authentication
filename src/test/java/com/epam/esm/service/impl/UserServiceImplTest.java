package com.epam.esm.service.impl;

import com.epam.esm.dto.mapper.impl.UserMapper;
import com.epam.esm.dto.request.UserDtoRequest;
import com.epam.esm.dto.response.UserDtoResponse;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ApplicationException;
import com.epam.esm.repository.user.UserRepository;
import com.epam.esm.security.jwt.JwtUser;
import com.epam.esm.security.jwt.config.JwtService;
import com.epam.esm.security.jwt.mapper.impl.JwtUserMapper;
import com.epam.esm.security.jwt.response.AuthenticationResponse;
import com.epam.esm.security.oauth.CustomOAuth2User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    private final String DEFAULT_NAME = "User";

    private final long DEFAULT_ID = 1;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private JwtUserMapper jwtUserMapper;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private MessageSource messageSource;

    @Mock
    private CustomOAuth2User OAuth2User;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findUserByIdTest() {
        User user = User.builder().id(DEFAULT_ID).build();

        Mockito.when(userRepository.findById(DEFAULT_ID)).thenReturn(Optional.of(user));
        UserDtoResponse resultUser = userService.getUserById(DEFAULT_ID);

        assertEquals(userMapper.toDTO(user), resultUser);
    }

    @Test
    void findUserByIdShouldThrowException() {
        Mockito.when(userRepository.findById(DEFAULT_ID)).thenReturn(Optional.empty());

        assertThrows(ApplicationException.class, () -> userService.getUserById(DEFAULT_ID));
    }

    @Test
    void findAllUsersTest() {
        Pageable pageable = PageRequest.of(0,10);
        List<User> users = List.of(User.builder().build());

        Mockito.when(userRepository.findAll(pageable)).thenReturn(new PageImpl<>(users));
        List<UserDtoResponse> responses = userService.getAllUsers(pageable);

        assertEquals(users.size(), responses.size());
    }

    @Test
    void registerTest(){
        UserDtoRequest request = UserDtoRequest.builder()
                .email("email@gmail.com")
                .firstName("Name")
                .lastName("Surname")
                .login("login")
                .password("qweqfw3v3").build();
        Mockito.when(userMapper.toEntity(request)).thenReturn(User.builder().role(new Role("User")).build());
        Mockito.when(userRepository.findRoleByName("User")).thenReturn(Optional.of(new Role("User")));
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(null);
        Mockito.when(jwtService.generateToken(Mockito.any())).thenReturn("");
        Mockito.when(jwtUserMapper.toUserDetails(Mockito.any())).thenReturn(JwtUser.builder().build());
        AuthenticationResponse response = userService.register(request);
        assertEquals("",response.getToken());
    }

    @Test
    void registerShouldThrowException(){
        Mockito.when(userRepository.findByLogin(Mockito.any())).thenReturn(Optional.of(User.builder().build()));

        assertThrows(ApplicationException.class, () -> userService.register(UserDtoRequest.builder().build()));
    }

    @Test
    void loginTest(){
        UserDtoRequest request = UserDtoRequest.builder()
                .email("email@gmail.com")
                .firstName("Name")
                .lastName("Surname")
                .login("login")
                .password("qweqfw3v3").build();

        Mockito.when(authenticationManager.authenticate(Mockito.any())).thenReturn(null);
        Mockito.when(userRepository.findByLogin(Mockito.anyString())).thenReturn(Optional.ofNullable(User.builder().build()));
        Mockito.when(jwtService.generateToken(Mockito.any())).thenReturn("");
        Mockito.when(jwtUserMapper.toUserDetails(Mockito.any())).thenReturn(JwtUser.builder().build());
        AuthenticationResponse response = userService.login(request);
        assertEquals("",response.getToken());
    }
    @Test
    void loginShouldThrowException() {
        Mockito.when(authenticationManager.authenticate(Mockito.any())).thenReturn(null);

        assertThrows(ApplicationException.class, () -> userService.login(UserDtoRequest.builder().build()));
    }
    @Test
    void oAuth2LoginTest() {

        Mockito.when(OAuth2User.getUniqueIndex()).thenReturn(DEFAULT_NAME);
        Mockito.when(userRepository.findByLogin(DEFAULT_NAME)).thenReturn(Optional.of(User.builder().build()));
        Mockito.when(jwtService.generateToken(Mockito.any())).thenReturn(DEFAULT_NAME);
        Mockito.when(jwtUserMapper.toUserDetails(Mockito.any())).thenReturn(JwtUser.builder().build());

        AuthenticationResponse response = userService.oAuthLogin(OAuth2User);

        assertEquals(DEFAULT_NAME,response.getToken());
    }
    @Test
    void oAuth2RegisterTest() {

        Mockito.when(OAuth2User.getUniqueIndex()).thenReturn(DEFAULT_NAME);
        Mockito.when(OAuth2User.getFirstName()).thenReturn(DEFAULT_NAME);
        Mockito.when(OAuth2User.getLastName()).thenReturn(DEFAULT_NAME);
        Mockito.when(OAuth2User.getEmail()).thenReturn(DEFAULT_NAME);
        Mockito.when(userRepository.findByLogin(DEFAULT_NAME)).thenReturn(Optional.empty());
        Mockito.when(userRepository.findRoleByName(Mockito.anyString())).thenReturn(Optional.of(new Role("User")));
        Mockito.when(jwtService.generateToken(Mockito.any())).thenReturn(DEFAULT_NAME);
        Mockito.when(jwtUserMapper.toUserDetails(Mockito.any())).thenReturn(JwtUser.builder().build());
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(null);

        AuthenticationResponse response = userService.oAuthLogin(OAuth2User);

        assertEquals(DEFAULT_NAME,response.getToken());
    }
}
