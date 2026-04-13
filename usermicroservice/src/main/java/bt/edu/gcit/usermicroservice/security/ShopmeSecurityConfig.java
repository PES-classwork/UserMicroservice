package bt.edu.gcit.usermicroservice.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.Arrays;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class ShopmeSecurityConfig {
    public ShopmeSecurityConfig() {
        System.out.println("ShopmeSecurityConfig created");
    }

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authProvider() {
    System.out.println("Initializing AuthenticationProvider...");

    // Pass the userDetailsService directly into the constructor
    DaoAuthenticationProvider authProvider = new
    DaoAuthenticationProvider(userDetailsService);

    // Set the password encoder using the setter
    authProvider.setPasswordEncoder(passwordEncoder());

    System.out.println("UserDetailsService assigned: " +
    userDetailsService.getClass().getName());
    return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
    .csrf(csrf -> csrf.disable())

    .sessionManagement(session ->
    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

    .authorizeHttpRequests(auth -> auth
    .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
    .requestMatchers(HttpMethod.POST, "/api/users").hasAuthority("Admin")
    .requestMatchers(HttpMethod.GET,
    "/api/users/checkDuplicateEmail").hasAuthority("Admin")
    .requestMatchers(HttpMethod.PUT, "/api/users/{id}").hasAuthority("Admin")
    .requestMatchers(HttpMethod.DELETE,"/api/users/{id}").hasAuthority("Admin")
    .requestMatchers(HttpMethod.PUT, "/api/users/{id}/enabled").permitAll()
    .requestMatchers(HttpMethod.GET, "/api/countries/**").permitAll()
    .requestMatchers(HttpMethod.POST, "/api/countries").permitAll()
    .requestMatchers(HttpMethod.PUT, "/api/countries").permitAll()
    .requestMatchers(HttpMethod.DELETE, "/api/countries").permitAll()
    .requestMatchers(HttpMethod.GET, "/api/states/**").permitAll()
    .requestMatchers(HttpMethod.POST, "/api/states/{country_id}").permitAll()
    .requestMatchers(HttpMethod.PUT, "/api/states").permitAll()
    .requestMatchers(HttpMethod.DELETE, "/api/states").permitAll()
    .requestMatchers(HttpMethod.POST, "/api/customer/*").permitAll())

    .addFilterBefore(jwtRequestFilter,
    UsernamePasswordAuthenticationFilter.class);

    return http.build();
    }
}
