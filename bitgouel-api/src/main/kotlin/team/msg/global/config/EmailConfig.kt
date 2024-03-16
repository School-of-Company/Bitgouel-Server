package team.msg.global.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import team.msg.global.config.properties.EmailProperties

@Configuration
class EmailConfig(
   private val emailProperties: EmailProperties
) {
    @Bean
    fun getJavaMailSender(): JavaMailSender =
        JavaMailSenderImpl().apply {
            this.host = emailProperties.host
            this.port = emailProperties.port
            this.username = emailProperties.username
            this.password = emailProperties.password
            this.javaMailProperties["mail.smtp.auth"] = true
            this.javaMailProperties["mail.smtp.connectiontimeout"] = 5000
            this.javaMailProperties["mail.smtp.timeout"] = 5000
            this.javaMailProperties["mail.smtp.writetimeout"] = 5000
            this.javaMailProperties["mail.transport.protocol"] = "smtp"
            this.javaMailProperties["mail.smtp.starttls.enable"] = true
            this.javaMailProperties["mail.smtp.starttls.required"] = true
        }
}