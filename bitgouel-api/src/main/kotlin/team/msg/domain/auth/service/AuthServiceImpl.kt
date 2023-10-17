package team.msg.domain.auth.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.msg.domain.auth.presentation.data.request.StudentSignUpWebRequest
import team.msg.domain.user.enums.Authority
import team.msg.domain.user.enums.SignUpStatus
import team.msg.domain.user.model.User
import team.msg.domain.user.repository.UserRepository
import java.util.*

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val securityUtil: team.msg.common.util.SecurityUtil
) : AuthService {

    @Transactional(rollbackFor = [Exception::class])
    override fun studentSignUp(studentSignUpWebRequest: StudentSignUpWebRequest) {
        val email = studentSignUpWebRequest.email
        val phoneNumber = studentSignUpWebRequest.phoneNumber

        if (userRepository.existsByEmailOrPhoneNumber(email, phoneNumber))
            throw team.msg.domain.auth.exception.AlreadySignUpException("이미 가입된 정보를 기입하였습니다.")

        val user = User(
            id = UUID.randomUUID(),
            email = email,
            name = studentSignUpWebRequest.name,
            phoneNumber = phoneNumber,
            password = securityUtil.passwordEncode(studentSignUpWebRequest.password),
            authority = Authority.ROLE_STUDENT,
            signUpStatus = SignUpStatus.PENDING
        )

        userRepository.save(user)
    }
}