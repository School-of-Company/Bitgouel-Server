package team.msg.thirdparty.aws.storage.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import team.msg.thirdparty.aws.AwsProperties

@Configuration
class AwsS3Config(
    private val awsProperties: AwsProperties
) {
}