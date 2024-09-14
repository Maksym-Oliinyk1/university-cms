package ua.com.foxminded.controllers;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ua.com.foxminded.security.AuthenticationService;
import ua.com.foxminded.security.JwtService;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class BaseSecurityTestClass {

    protected static final String token = "mockedJwtToken";
    protected static final String userEmail = "user@example.com";
    @MockBean
    protected AuthenticationService authenticationService;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private UserDetailsService userDetailsService;

    protected void configureSecurity() {
        when(jwtService.extractUsername(anyString())).thenReturn(userEmail);
        when(jwtService.isTokenValid(eq(token), any(UserDetails.class))).thenReturn(true);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(userEmail)).thenReturn(userDetails);
    }
}
