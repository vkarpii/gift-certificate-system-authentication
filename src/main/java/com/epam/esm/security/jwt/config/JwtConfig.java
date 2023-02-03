package com.epam.esm.security.jwt.config;

import com.epam.esm.exception.ApplicationException;
import com.epam.esm.exception.ExceptionMessage;
import com.epam.esm.repository.user.UserRepository;
import com.epam.esm.security.jwt.mapper.impl.JwtUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
public class JwtConfig {

    private final UserRepository repository;

    private final JwtUserMapper mapper;

    public JwtConfig(UserRepository repository, JwtUserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }


    @Bean
    public UserDetailsService userDetailsService(){
        return username -> mapper.toUserDetails(repository.findByLogin(username).orElseThrow(() ->{
            log.info(ExceptionMessage.USER_NOT_FOUND);
            return new ApplicationException(ExceptionMessage.USER_NOT_FOUND);
        }));
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
