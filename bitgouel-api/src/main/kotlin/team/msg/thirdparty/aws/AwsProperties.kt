package team.msg.thirdparty.aws

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "cloud.aws.credentials")
@ConstructorBinding
class AwsProperties(
    val accessKey: String,
    val secretKey: String
)