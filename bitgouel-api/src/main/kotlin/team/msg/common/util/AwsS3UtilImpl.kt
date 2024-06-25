package team.msg.common.util

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.DeleteObjectRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import team.msg.global.exception.InternalServerException
import team.msg.thirdparty.aws.s3.AwsS3Util
import team.msg.thirdparty.aws.s3.properties.AwsS3Properties

@Component
class AwsS3UtilImpl(
    private val amazonS3: AmazonS3,
    private val awsS3Properties: AwsS3Properties
) : AwsS3Util{

    override fun uploadImage(multipartFile: MultipartFile, fileName: String): String {
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
        }.onFailure { e ->
            println(e.message)
            println(e.printStackTrace())
            throw InternalServerException("이미지 업로드에 실패했습니다. ${e.message} + ${e.printStackTrace()}")
        }

        val imageUrl = amazonS3.getUrl(awsS3Properties.bucket, fileName).toString()

        return imageUrl
    }

    override fun deleteImage(fileName: String) {
        amazonS3.deleteObject(DeleteObjectRequest(awsS3Properties.bucket, fileName))
    }

}