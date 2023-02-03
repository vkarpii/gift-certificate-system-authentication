package com.epam.esm.controller;

import com.epam.esm.assembler.UserAssembler;
import com.epam.esm.dto.request.UserDtoRequest;
import com.epam.esm.dto.response.UserDtoResponse;
import com.epam.esm.security.jwt.response.AuthenticationResponse;
import com.epam.esm.security.oauth.CustomOAuth2User;
import com.epam.esm.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService service;

    private final UserAssembler assembler;

    @Autowired
    public UserController(UserService service, UserAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @PreAuthorize("hasAnyAuthority('Admin','User')")
    @GetMapping("/{id}")
    public UserDtoResponse find(@PathVariable long id) {
        return assembler.toModel(service.getUserById(id));
    }

    @PreAuthorize("hasAnyAuthority('Admin')")
    @GetMapping
    public List<UserDtoResponse> findAll(Pageable pageable) {
        return assembler.toCollectionModel(service.getAllUsers(pageable))
                .getContent().stream().toList();
    }

    @PostMapping("/register")
    public AuthenticationResponse register(@RequestBody UserDtoRequest user){
        return service.register(user);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody UserDtoRequest user){
        return service.login(user);
    }

    @GetMapping("oauth2/login")
    public AuthenticationResponse oAuthLogin(@AuthenticationPrincipal OAuth2User principal){
        return service.oAuthLogin(new CustomOAuth2User(principal));
    }

}
