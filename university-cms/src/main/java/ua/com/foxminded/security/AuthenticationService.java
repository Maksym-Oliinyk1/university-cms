package ua.com.foxminded.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.com.foxminded.dto.AdministratorDTO;
import ua.com.foxminded.dto.MaintainerDTO;
import ua.com.foxminded.dto.StudentDTO;
import ua.com.foxminded.dto.TeacherDTO;
import ua.com.foxminded.entity.Administrator;
import ua.com.foxminded.entity.Maintainer;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.service.AdministratorService;
import ua.com.foxminded.service.MaintainerService;
import ua.com.foxminded.service.StudentService;
import ua.com.foxminded.service.TeacherService;

@Service
public class AuthenticationService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(JwtService jwtService,
                                 AuthenticationManager authenticationManager) {

        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }


    public AuthenticationResponse signIn(AuthenticationRequest request) {

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            ));

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtService.generateToken(userDetails);
            return new AuthenticationResponse(jwt);
        } catch (BadCredentialsException e) {
            throw new UsernameNotFoundException("Invalid email or password");
        }
    }
}
