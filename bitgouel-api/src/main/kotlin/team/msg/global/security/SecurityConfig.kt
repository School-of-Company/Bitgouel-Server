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
    companion object {
        const val USER = "USER"
        const val ADMIN = "ADMIN"
        const val STUDENT = "STUDENT"
        const val TEACHER = "TEACHER"
        const val BBOZZAK = "BBOZZAK"
        const val PROFESSOR = "PROFESSOR"
        const val COMPANY_INSTRUCTOR = "COMPANY_INSTRUCTOR"
        const val GOVERNMENT = "GOVERNMENT"
    }

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

            // health
            .mvcMatchers(HttpMethod.GET, "/").permitAll()

            // auth
            .mvcMatchers(HttpMethod.POST, "/auth/student").permitAll()
            .mvcMatchers(HttpMethod.POST, "/auth/teacher").permitAll()
            .mvcMatchers(HttpMethod.POST, "/auth/bbozzak").permitAll()
            .mvcMatchers(HttpMethod.POST, "/auth/professor").permitAll()
            .mvcMatchers(HttpMethod.POST, "/auth/government").permitAll()
            .mvcMatchers(HttpMethod.POST, "/auth/company-instructor").permitAll()
            .mvcMatchers(HttpMethod.POST, "/auth/login").permitAll()
            .mvcMatchers(HttpMethod.PATCH, "/auth").permitAll()
            .mvcMatchers(HttpMethod.DELETE, "/auth").authenticated()
            .mvcMatchers(HttpMethod.DELETE, "/auth/withdraw").authenticated()

            // activity
            .mvcMatchers(HttpMethod.POST, "/activity").hasRole(STUDENT)
            .mvcMatchers(HttpMethod.PATCH, "/activity/{id}").hasRole(STUDENT)
            .mvcMatchers(HttpMethod.PATCH, "/activity/{id}/approve").hasRole(TEACHER)
            .mvcMatchers(HttpMethod.DELETE, "/activity/{id}").hasRole(STUDENT)
            .mvcMatchers(HttpMethod.DELETE, "/activity/{id}/reject").hasRole(TEACHER)
            .mvcMatchers(HttpMethod.GET, "/activity").hasRole(ADMIN)

            // lecture
            .mvcMatchers(HttpMethod.POST, "/lecture").hasAnyRole(PROFESSOR, COMPANY_INSTRUCTOR, GOVERNMENT)
            .mvcMatchers(HttpMethod.POST, "/lecture/{id}").hasRole(STUDENT)
            .mvcMatchers(HttpMethod.PATCH, "/lecture/{id}/approve").hasRole(ADMIN)
            .mvcMatchers(HttpMethod.DELETE, "/lecture/{id}/reject").hasRole(ADMIN)

            // faq
            .mvcMatchers(HttpMethod.POST, "/faq").hasRole(ADMIN)
            .mvcMatchers(HttpMethod.GET, "/faq").permitAll()
            .mvcMatchers(HttpMethod.GET, "/faq/{id}").permitAll()

            // user
            .mvcMatchers(HttpMethod.GET, "/user").authenticated()

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