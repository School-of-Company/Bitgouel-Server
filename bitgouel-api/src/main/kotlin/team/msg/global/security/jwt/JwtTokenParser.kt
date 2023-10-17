package team.msg.global.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.InvalidClaimException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import javax.servlet.http.HttpServletRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import team.msg.global.exception.ExpiredTokenException
import team.msg.global.exception.InternalServerException
import team.msg.global.exception.InvalidTokenException
import team.msg.global.security.jwt.properties.JwtProperties
import team.msg.global.security.principal.AuthDetailsService
import java.security.Key

@Component
class JwtTokenParser(
    private val jwtProperties: JwtProperties,
    private val authDetailsService: AuthDetailsService
) {
    fun parseAccessToken(request: HttpServletRequest): String? =
        request.getHeader(JwtProperties.tokenHeader)
            .let { it ?: return null }
            .let { if (it.startsWith(JwtProperties.tokenPrefix))
                it.replace(JwtProperties.tokenPrefix, "")
            else null }

    fun parseRefreshToken(refreshToken: String): String? =
        if (refreshToken.startsWith(JwtProperties.tokenPrefix))
            refreshToken.replace(JwtProperties.tokenPrefix, "")
        else null

    fun authentication(accessToken: String): Authentication =
        authDetailsService.loadUserByUsername(getTokenBody(accessToken, jwtProperties.accessSecret).subject)
            .let { UsernamePasswordAuthenticationToken(it, "", it.authorities) }

    private fun getTokenBody(token: String, secret: Key): Claims =
        try {
            Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .body
        } catch (e: Exception) {
            when (e) {
                is ExpiredJwtException -> throw ExpiredTokenException("토큰이 만료되었습니다.")
                is InvalidClaimException -> throw InvalidTokenException("유효하지 않은 토큰입니다.")
                is JwtException -> throw InvalidTokenException("유효하지 않은 토큰입니다.")
                else -> throw InternalServerException("Server Error")
            }
        }
}