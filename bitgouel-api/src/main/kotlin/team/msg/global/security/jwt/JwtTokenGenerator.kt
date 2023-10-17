package team.msg.global.security.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import team.msg.domain.auth.model.RefreshToken
import team.msg.domain.auth.presentation.data.response.TokenResponse
import team.msg.domain.auth.repository.RefreshTokenRepository
import team.msg.domain.user.enums.Authority
import team.msg.global.security.jwt.properties.JwtProperties
import java.security.Key
import java.time.LocalDateTime
import java.util.*

@Component
class JwtTokenGenerator(
    private val jwtProperties: JwtProperties,
    private val refreshTokenRepository: RefreshTokenRepository
) {
    fun generateToken(userId: UUID, authority: Authority): TokenResponse {
        val accessToken = generateAccessToken(userId, jwtProperties.accessSecret, authority)
        val refreshToken = generateRefreshToken(userId, jwtProperties.refreshSecret, authority)
        val accessExpiredAt = getAccessTokenExpiredAt()
        val refreshExpiredAt = getRefreshTokenExpiredAt()
        refreshTokenRepository.save(RefreshToken(refreshToken, userId, authority, jwtProperties.refreshExpiration))
        return TokenResponse(accessToken, refreshToken, accessExpiredAt, refreshExpiredAt)
    }

    private fun generateAccessToken(userId: UUID, secret: Key, authority: Authority): String =
        Jwts.builder()
            .signWith(secret, SignatureAlgorithm.HS256)
            .setSubject(userId.toString())
            .claim("type", JwtProperties.accessType)
            .claim(JwtProperties.roleType, authority)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + jwtProperties.accessExpiration * 1000))
            .compact()

    private fun generateRefreshToken(userId: UUID, secret: Key, authority: Authority): String =
        Jwts.builder()
            .signWith(secret, SignatureAlgorithm.HS256)
            .setSubject(userId.toString())
            .claim("type", JwtProperties.refreshType)
            .claim(JwtProperties.roleType, authority)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + jwtProperties.refreshExpiration * 1000))
            .compact()

    private fun getAccessTokenExpiredAt(): LocalDateTime =
        LocalDateTime.now().plusSeconds(jwtProperties.accessExpiration.toLong())

    private fun getRefreshTokenExpiredAt(): LocalDateTime =
        LocalDateTime.now().plusSeconds(jwtProperties.refreshExpiration.toLong())
}