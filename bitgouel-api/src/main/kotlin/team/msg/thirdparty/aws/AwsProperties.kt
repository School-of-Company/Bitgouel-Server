package team.msg.thirdparty.aws

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "cloud.aws.s3")
@ConstructorBinding
class AwsProperties(
    val bucket: String
)