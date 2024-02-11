package az.najafov.deforestationnews.configuration;

import az.najafov.deforestationnews.common.SecurityUtil;
import az.najafov.deforestationnews.exception.JWTAccessDeniedHandler;
import az.najafov.deforestationnews.exception.JwtAuthenticationEntryPoint;
import az.najafov.deforestationnews.security.filter.AuthorizationFilter;
import az.najafov.deforestationnews.security.userdetails.CustomUserDetailsService;
import az.najafov.deforestationnews.security.filter.JwtAuthenticationFilter;
import az.najafov.deforestationnews.security.provider.CustomAuthenticationProvider;
import az.najafov.deforestationnews.security.provider.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfiguration {


    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final CustomUserDetailsService userDetailsService;
    private final TokenProvider tokenProvider;
    private final SecurityUtil securityUtil;
    private final JWTAccessDeniedHandler accessDeniedHandler;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private static final String[] AUTH_WHITELIST = {

            // for Swagger UI v2
            "/v2/api-docs",
            "/swagger-ui.html",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/webjars/**",
            // for Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/images/**",
            "/api/auth/register",
            "/api/auth/login",
            "/api/auth/refresh",
            "api/cities/countries/{id}",
            "api/regions/countries/{id}",
            "api/regions/cities/{id}",
            "api/regions/districts/{id}",
            "api/districts/cities/{id}"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests().anyRequest()
//                .permitAll()
                .authenticated()
                .and()
                .sessionManagement((sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)))
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(tokenProvider, userDetailsService),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new AuthorizationFilter(securityUtil),
                        JwtAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        return (web) -> web.ignoring()
                .requestMatchers(AUTH_WHITELIST)
                .requestMatchers(HttpMethod.GET, "/api/news/{id}")
                .requestMatchers(HttpMethod.GET, "/api/news")
                .requestMatchers(HttpMethod.GET, "api/countries")
                .requestMatchers(HttpMethod.GET, "api/cities")
                .requestMatchers(HttpMethod.GET, "api/districts")
                .requestMatchers(HttpMethod.GET, "api/regions")
                .requestMatchers(HttpMethod.GET, "api/news/{id}/comments")
                .requestMatchers(HttpMethod.GET, "apis/news/{id}/comments/{id}");
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(customAuthenticationProvider);
        return authenticationManagerBuilder.build();
    }

}
