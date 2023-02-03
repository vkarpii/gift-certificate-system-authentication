package com.epam.esm.service;

import com.epam.esm.dto.request.UserDtoRequest;
import com.epam.esm.dto.response.UserDtoResponse;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ApplicationException;
import com.epam.esm.security.jwt.response.AuthenticationResponse;
import com.epam.esm.security.oauth.CustomOAuth2User;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;

/**
 * This interface represents Service implementation that connected controller with Data Access Object.
 *
 * @param <T> has to implement {@link User} interface
 * @author Vitaly Karpii
 * @see User
 */
public interface UserService {

    /**
     * This method return user by his id.
     *
     * @return {@link UserDtoResponse}
     * @throws {@link ApplicationException} in case if tag not found with searched id.
     */
    UserDtoResponse getUserById(long id);

    /**
     * This method return all active users with criteria.
     *
     * @return list of{@link UserDtoResponse}
     */
    List<UserDtoResponse> getAllUsers(Pageable pageable);

    /**
     * This method return user by his id.
     *
     * @return {@link User}
     * @throws {@link ApplicationException} in case if tag not found with searched id.
     */
    User getUser(long id);

    AuthenticationResponse register(UserDtoRequest user);

    AuthenticationResponse login(UserDtoRequest user);

    AuthenticationResponse oAuthLogin(CustomOAuth2User OAuth2User);
}
