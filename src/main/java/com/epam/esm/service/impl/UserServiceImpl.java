package com.epam.esm.service.impl;

import com.epam.esm.dto.mapper.impl.UserMapper;
import com.epam.esm.dto.request.UserDtoRequest;
import com.epam.esm.dto.response.UserDtoResponse;

import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ApplicationException;
import com.epam.esm.exception.ExceptionMessage;
import com.epam.esm.repository.user.UserRepository;
import com.epam.esm.security.jwt.config.JwtService;
import com.epam.esm.security.jwt.mapper.impl.JwtUserMapper;
import com.epam.esm.security.jwt.response.AuthenticationResponse;
import com.epam.esm.security.oauth.CustomOAuth2User;
import com.epam.esm.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final JwtService jwtService;

    private final UserRepository repository;

    private final UserMapper userMapper;

    private final JwtUserMapper jwtUserMapper;

    private final MessageSource messageSource;

    private final PasswordEncoder encoder;

    private final AuthenticationManager authenticationManager;

   /* @PostConstruct
    public void generateUsers(){
        log.info("Started generate USERS...");
        Role role = repository.findRoleByName("User").orElseThrow();
        List<String> domains = List.of("gmail.com","ukr.net","domain.ua","mail.ua");
        List<String> element = List.of("_",".","");
        Random random = new Random();
        Faker randomData = new Faker(Locale.ENGLISH);
        List<User> users = IntStream.rangeClosed(1,1000)
                .mapToObj(index ->  {
                    User user = new User();
                    user.setFirstName(randomData.name().firstName());
                    user.setLastName(randomData.name().lastName());
                    user.setEmail(user.getFirstName() + element.get(random.nextInt(element.size())) +
                            user.getLastName() + "@" + domains.get(random.nextInt(domains.size())));
                    user.setLogin(user.getFirstName() + element.get(random.nextInt(element.size())) +
                            user.getLastName() + random.nextInt(99999));
                    user.setPassword(encoder.encode(user.getLastName() + random.nextInt()));
                    user.setRole(role);
                    return user;
                }).toList();
        repository.saveAll(users);
        log.info("USERS generated!");
    }*/

    @Autowired
    public UserServiceImpl(JwtService jwtService, UserRepository repository, UserMapper userMapper, JwtUserMapper jwtUserMapper, MessageSource messageSource, PasswordEncoder encoder, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.repository = repository;
        this.userMapper = userMapper;
        this.jwtUserMapper = jwtUserMapper;
        this.messageSource = messageSource;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public UserDtoResponse getUserById(long id) {
        return userMapper.toDTO(getUser(id));
    }

    @Override
    public List<UserDtoResponse> getAllUsers(Pageable pageable) {
        List<User> users = repository.findAll(pageable).getContent();
        return users.stream().map(userMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public User getUser(long id) {
        return repository.findById(id).orElseThrow(() -> {
            log.error(messageSource.getMessage(ExceptionMessage.USER_NOT_FOUND, new Object[]{},
                    LocaleContextHolder.getLocale()));
            return new ApplicationException(ExceptionMessage.USER_NOT_FOUND);
        });
    }

    @Override
    @Transactional
    public AuthenticationResponse register(UserDtoRequest request) {
        Optional<User> exist = repository.findByLogin(request.getLogin());
        if (exist.isPresent()){
            log.error(messageSource.getMessage(ExceptionMessage.USER_ID_EXIST, new Object[]{},
                    LocaleContextHolder.getLocale()));
            throw new ApplicationException(ExceptionMessage.USER_ID_EXIST);
        }
        User user = userMapper.toEntity(request);
        user.setRole(repository.findRoleByName(user.getRole().getName()).orElseThrow());
        repository.save(user);
        return createResponseToken(user);
    }

    @Override
    @Transactional
    public AuthenticationResponse login(UserDtoRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getLogin(),request.getPassword())
        );
        User user = repository.findByLogin(request.getLogin()).orElseThrow(() -> {
            log.error(messageSource.getMessage(ExceptionMessage.USER_NOT_FOUND, new Object[]{},
                    LocaleContextHolder.getLocale()));
            throw new ApplicationException(ExceptionMessage.USER_NOT_FOUND);
        });
        return createResponseToken(user);
    }

    @Override
    public AuthenticationResponse oAuthLogin(CustomOAuth2User OAuth2User) {
        Optional<User> user = repository.findByLogin(OAuth2User.getUniqueIndex());
        if (user.isPresent()){
            return createResponseToken(user.get());
        }
        User newUser = User.builder()
                .role(repository.findRoleByName("User").orElseThrow())
                .email(OAuth2User.getEmail())
                .firstName(OAuth2User.getFirstName())
                .lastName(OAuth2User.getLastName())
                .login(OAuth2User.getUniqueIndex())
                .build();
        repository.save(newUser);
        return createResponseToken(newUser);
    }

    private AuthenticationResponse createResponseToken(User user){
        return AuthenticationResponse.builder()
                .token(jwtService.generateToken(jwtUserMapper.toUserDetails(user)))
                .build();
    }
}
