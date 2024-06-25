package team.msg.thirdparty.aws.s3

import org.springframework.web.multipart.MultipartFile

interface AwsS3Util {
    fun uploadImage(multipartFile: MultipartFile, fileName: String): String
    fun deleteImage(fileName: String)
}