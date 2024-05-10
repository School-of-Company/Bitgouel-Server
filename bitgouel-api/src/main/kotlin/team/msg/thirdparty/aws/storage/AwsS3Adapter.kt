package team.msg.thirdparty.aws.storage

import com.amazonaws.HttpMethod
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import team.msg.common.util.FileUtil
import team.msg.thirdparty.aws.storage.properties.AwsS3Properties
import java.sql.Timestamp
import java.time.LocalDateTime

@Component
class AwsS3Adapter(
    private val awsS3Properties: AwsS3Properties,
    private val amazonS3: AmazonS3
) : FileUtil {
    override fun upload(multipartFile: MultipartFile, fileName: String) {
        inputS3(multipartFile, fileName)

        getUploadUrl(fileName)
    }

    private fun inputS3(multipartFile: MultipartFile, fileName: String) {
        val objectMetadata = ObjectMetadata()

        objectMetadata.contentLength = multipartFile.size
        objectMetadata.contentType = multipartFile.contentType

        runCatching {
            amazonS3.putObject(
                PutObjectRequest(awsS3Properties.bucket, fileName, multipartFile.inputStream, objectMetadata)
                    .withCannedAcl(
                        CannedAccessControlList.PublicRead
                    )
            )
        }.onFailure {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다.")
        }
    }

    private fun getUploadUrl(fileName: String): String =
        amazonS3.getUrl(awsS3Properties.bucket, fileName).toString()
}