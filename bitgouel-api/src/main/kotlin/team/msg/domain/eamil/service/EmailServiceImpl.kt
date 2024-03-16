package team.msg.domain.eamil.service

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.thymeleaf.context.Context
import org.thymeleaf.spring5.SpringTemplateEngine
import team.msg.domain.eamil.exception.AlreadyAuthenticatedEmailException
import team.msg.domain.eamil.exception.EmailSendFailException
import team.msg.domain.eamil.exception.TooManyEmailAuthenticationRequestException
import team.msg.domain.eamil.presentation.data.request.SendAuthenticationEmailRequestData
import team.msg.domain.email.model.EmailAuthentication
import team.msg.domain.email.repository.EmailAuthenticationRepository
import team.msg.global.config.properties.EmailProperties
import java.util.*

@Service
class EmailServiceImpl(
    private val emailAuthenticationRepository: EmailAuthenticationRepository,
    private val templateEngine: SpringTemplateEngine,
    private val mailSender: JavaMailSender,
    private val emailProperties: EmailProperties
) : EmailService {
    /**
     * 입력된 email로 인증 링크를 전송하는 비지니스 로직입니다.
     * @param 인증 링크가 전송될 email이 담긴 dto
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun sendAuthenticationEmail(request: SendAuthenticationEmailRequestData) {
        val email = request.email
        val code = UUID.randomUUID().toString()

        val emailAuthentication = emailAuthenticationRepository.findById(email)
            .orElse(
                EmailAuthentication(
                    email = email,
                    isAuthentication = false,
                    code = code
                )
            )

        if(emailAuthentication.isAuthentication)
            throw AlreadyAuthenticatedEmailException("이미 인증된 이메일입니다. info : [ email = $email ]")

        if(emailAuthentication.attemptCount >= 3)
            throw TooManyEmailAuthenticationRequestException("너무 많은 이메일 인증 요청을 보냈습니다. info : [ email = $email ]")

        val updatedEmailAuthentication = emailAuthentication.run {
            EmailAuthentication(
                email = email,
                isAuthentication = false,
                code = code,
                attemptCount = attemptCount + 1
            )
        }

        emailAuthenticationRepository.save(updatedEmailAuthentication)

        runCatching {
            val message = mailSender.createMimeMessage()
            val helper = MimeMessageHelper(message, "UTF-8")
            val mailTemplate = createAuthenticationEmailTemplate(email, code)

            helper.setSubject("빛고을 이메일 인증")
            helper.setTo(email)
            helper.setText(mailTemplate, true)
            mailSender.send(message)
        }.onFailure {
            throw EmailSendFailException("이메일 전송에 실패했습니다. info : [ email = $email ]")
        }

    }

    /**
     * 인증 메일 템플릿을 생성하는 비지니스 로직입니다.
     * authentication-email-template 경로의 파일로 인증 메일에 필요한 html 템플릿을 생성합니다.
     * @param 인증할 이메일과 생성된 인증 코드
     * @return 인증 메일에 발송할 html 쳄플릿
     */
    private fun createAuthenticationEmailTemplate(email: String, code: String): String {
        val context = Context()
        val url = "${emailProperties.url}/email/?email=${email}&code=${code}"

        context.setVariable("url", url)

        return templateEngine.process("authentication-email-template", context)
    }
}