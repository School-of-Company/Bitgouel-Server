package team.msg.domain.auth.repository

import org.springframework.data.repository.CrudRepository
import team.msg.domain.auth.model.RefreshToken

interface RefreshTokenRepository : CrudRepository<RefreshToken, String>