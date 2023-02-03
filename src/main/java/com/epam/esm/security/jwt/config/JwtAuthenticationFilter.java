package com.epam.esm.security.jwt.config;

import com.epam.esm.exception.ExceptionMessage;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String REQUEST_HEADER = "Authorization";

    private final JwtService service;

    private final UserDetailsService userDetailsService;

    private final MessageSource messageSource;

    public JwtAuthenticationFilter(JwtService service, UserDetailsService userDetailsService, MessageSource messageSource) {
        this.service = service;
        this.userDetailsService = userDetailsService;
        this.messageSource = messageSource;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader(REQUEST_HEADER);
        final String jwt;
        final String login;
        if (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)){
            filterChain.doFilter(request,response);
            return;
        }
        jwt = authHeader.substring(TOKEN_PREFIX.length());
        try{
            login = service.extractLogin(jwt);
        }catch (MalformedJwtException | ExpiredJwtException exception){
            handleExpiredJwtException(exception,response);
            return;
        }

        if (login != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(login);
            if (service.isTokenValid(jwt,userDetails)){
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(token);
            }
        }
        filterChain.doFilter(request,response);
    }

    private void handleExpiredJwtException(Exception exception,HttpServletResponse response) throws IOException {
        String message = messageSource.getMessage(ExceptionMessage.TOKEN_EXPIRED, new Object[]{}, LocaleContextHolder.getLocale());
        log.error(message);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("{\n" +
                "    \"errorMessage\": \""+ message +
                "\" \n    \"errorCode\": \"401-00\"\n" +
                "}");
    }
}
