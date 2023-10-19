package team.msg.domain.student.presentation.data.web

import javax.validation.constraints.Max
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import java.time.LocalDateTime

data class CreateStudentActivityWebRequest(
    @field:NotBlank
    @field:Max(100)
    val title: String,

    @field:NotBlank
    @field:Max(1000)
    val content: String,

    @field:NotNull
    val credit: Int,

    @field:NotNull
    val createdAt: LocalDateTime
)