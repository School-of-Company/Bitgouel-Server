package team.msg.domain.email.repository

import org.springframework.data.repository.CrudRepository
import team.msg.domain.email.model.EmailAuthentication

interface EmailAuthenticationRepository : CrudRepository<EmailAuthentication,String>