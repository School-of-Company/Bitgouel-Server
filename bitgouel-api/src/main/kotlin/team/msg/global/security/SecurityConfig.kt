package team.msg.global.security

import team.msg.global.config.FilterConfig
import team.msg.global.security.handler.CustomAccessDeniedHandler
import team.msg.global.security.handler.CustomAuthenticationEntryPointHandler
import team.msg.global.security.jwt.JwtTokenParser
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.cors.CorsUtils

@EnableWebSecurity
class SecurityConfig(
    private val jwtTokenParser: JwtTokenParser
) {
    @Bean
    protected fun filterChain(http: HttpSecurity): SecurityFilterChain =
        http
            .cors()
            .and()
            .csrf().disable()

            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .requestMatchers(RequestMatcher { request ->
                CorsUtils.isPreFlightRequest(request)
            }).permitAll()

            .mvcMatchers(HttpMethod.POST, "/auth/student").permitAll()
            .mvcMatchers(HttpMethod.POST, "/auth/teacher").permitAll()

            .anyRequest().authenticated()
            .and()

            .exceptionHandling()
            .authenticationEntryPoint(CustomAuthenticationEntryPointHandler())
            .accessDeniedHandler(CustomAccessDeniedHandler())
            .and()

            .apply(FilterConfig(jwtTokenParser))
            .and()
            .build()

    @Bean
    protected fun passwordEncode() = BCryptPasswordEncoder()
}