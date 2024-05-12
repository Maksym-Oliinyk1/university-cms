package ua.com.foxminded.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private static final String ALL_AUTHORITIES = "ADMINISTRATOR, MAINTAINER, STUDENT, TEACHER";

    private static final String[] WHITE_LIST_URL = {
            "/showLecture",
            "/showGroup",
            "/showCourse",
            "/showFaculty",
            "/createFormStudent",
            "/createFormTeacher",
            "/auth/student/register",
            "/auth/teacher/register",
            "/auth/admin/register",
            "/auth/maintainer/register",
            "/getList/**",
            "/showStudent",
            "/auth/authenticate",
            "/auth/**",
            "/general/**",
            "/showImages/**",
            "/listStudentsByGroup",
            "/css/**"
    };

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthFilter, AuthenticationProvider authenticationProvider) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL)
                                .permitAll()
                                .requestMatchers("/auth/successful").hasAnyAuthority("STUDENT", "ADMINISTRATOR", "TEACHER", "MAINTAINER")
                                .requestMatchers("/general/**").hasAnyAuthority("STUDENT", "ADMINISTRATOR", "TEACHER", "MAINTAINER")
                                .requestMatchers("/manage/**").hasAnyAuthority("MAINTAINER", "ADMINISTRATOR")
                                .requestMatchers("/maintainer/**").hasRole("MAINTAINER")
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessHandler((req, res, authentication) -> res.setStatus(HttpServletResponse.SC_OK))
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID", "jwtToken")
                );

        return http.build();
    }
}
