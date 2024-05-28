package com.bunyaminkalkan.api.config;

import com.bunyaminkalkan.api.security.CustomAuthenticationEntryPoint;
import com.bunyaminkalkan.api.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/users").permitAll()
                                .requestMatchers(HttpMethod.POST, "/users").authenticated()
                                .requestMatchers(HttpMethod.GET, "/users/{id}").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/users/{id}").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/users/{id}").authenticated()
                                .requestMatchers(HttpMethod.GET, "/users/{id}/profilePhoto").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/users/{id}/profilePhoto").authenticated()
                                .requestMatchers(HttpMethod.GET, "/posts").permitAll()
                                .requestMatchers(HttpMethod.POST, "/posts").authenticated()
                                .requestMatchers(HttpMethod.GET, "/posts/{postId}").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/posts/{postId}").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/posts/{postId}").authenticated()
                                .requestMatchers(HttpMethod.POST, "/posts/{postId}/**").authenticated()
                                .requestMatchers(HttpMethod.GET, "/comments").permitAll()
                                .requestMatchers(HttpMethod.POST, "/comments").authenticated()
                                .requestMatchers(HttpMethod.GET, "/comments/{commentId}").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/comments/{commentId}").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/comments/{commentId}").authenticated()
                                .requestMatchers(HttpMethod.POST, "/comments/{commentId}/**").authenticated()
                                .requestMatchers(HttpMethod.GET, "/universities/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/departments").permitAll()
                                .anyRequest().denyAll())
                .httpBasic(basic -> basic.authenticationEntryPoint(customAuthenticationEntryPoint))
                .exceptionHandling(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
