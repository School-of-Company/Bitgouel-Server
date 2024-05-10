package team.msg.common.util

import org.springframework.web.multipart.MultipartFile

interface FileUtil {
    fun upload(multipartFile: MultipartFile, fileName: String)
}