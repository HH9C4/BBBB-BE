package com.sdy.bbbb.config;

import com.sdy.bbbb.exception.AuthenticationEntryPointException;
import com.sdy.bbbb.jwt.JwtAuthFilter;
import com.sdy.bbbb.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@ConditionalOnDefaultWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;

    private static final String[] PERMIT_URL_ARRAY = {
            /* swagger v3 */
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-resources/**"
    };      // swagger 열어주기

    private final AuthenticationEntryPointException authenticationEntryPointException;

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer ignoringCustomizer() {

        return (web) -> web.ignoring().antMatchers("/h2-console/**");
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();


        corsConfiguration.setAllowedOriginPatterns(Arrays.asList("*"));
//        corsConfiguration.addAllowedOrigin("*");
//        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
        corsConfiguration.addAllowedOrigin("http://localhost:3000");
        corsConfiguration.addAllowedOrigin("https://www.boombiboombi.com");
        corsConfiguration.addAllowedOrigin("http://boombiboombi.o-r.kr");
        corsConfiguration.addAllowedOrigin("https://boombiboombi.o-r.kr");
//        corsConfiguration.addAllowedOrigin("127.0.0.1");

//        corsConfiguration.addAllowedOrigin("*");
//        corsConfiguration.addAllowedOrigin("http://3.36.98.254/");
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.setAllowedMethods(Arrays.asList("POST", "GET", "DELETE", "PUT"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addExposedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.cors();
        http.csrf().disable();


        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPointException);

        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/user/signin/**").permitAll()
                .antMatchers(HttpMethod.GET, "/user/tester/**").permitAll()
                .antMatchers(HttpMethod.GET, "/user/tester2/**").permitAll()
//                .antMatchers(HttpMethod.GET, "/user/reissue").authenticated()
                .antMatchers("/api/maininfo").permitAll()
                .antMatchers("/api/guinfo").permitAll()
                .antMatchers("/healthtest").permitAll()
                .antMatchers(PERMIT_URL_ARRAY).permitAll() //swagger 열어주기
                .antMatchers ( "/ws/**" ).permitAll()
                .anyRequest().authenticated()
//                .anyRequest().authenticated() //permitAll을 제외한 API는 모두 인증 필요
                .and()
                .addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();


    }
}
