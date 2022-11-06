package com.novi.fassignment.config;

import com.google.inject.internal.util.ImmutableList;
import com.novi.fassignment.filter.JwtRequestFilter;
import com.novi.fassignment.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //JWT token authentication
        //http.cors().and().csrf().disable() // I removed last minute on 4-11-22 because cors error solved with @CORS works well
        http.csrf().disable()
                .authorizeRequests().
                /* antMatchers(HttpMethod.POST, "http://localhost:8080/api/user/questions-upload-without-files").permitAll().
                 antMatchers(HttpMethod.POST, "http://localhost:8080/api/user/questions-upload-with-files-in-database").permitAll().
                 antMatchers(HttpMethod.PUT, "http://localhost:8080/api/user/questions-edit-with-files/{id}").permitAll().*/
                anyRequest().permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    }


/*    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().disable()
                .cors(c -> {
            CorsConfigurationSource source = request -> {
                CorsConfiguration config = new CorsConfiguration();

                config.setAllowedOrigins(
                        //List.of("example.com", "example.org"));
                        List.of("http://localhost:8081","http://localhost:8080", "http://localhost:8080/api/auth/signup"));
                config.setAllowedMethods(
                        List.of("GET", "POST", "PUT", "DELETE"));
                return config;
            };
            c.configurationSource(source);
        });
        http.csrf().disable();
        http.authorizeRequests()
                .anyRequest().permitAll();
    }*/


/*    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        //configuration.setAllowedOrigins(Arrays.asList("https://example.com"));
        //configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080","http://localhost:8081","http://localhost:3000"));
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }*/


}