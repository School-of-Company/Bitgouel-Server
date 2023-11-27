package team.msg.domain.student.presentation.data.web

import com.fasterxml.jackson.annotation.JsonFormat
import javax.validation.constraints.Max
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import java.time.LocalDate

data class UpdateStudentActivityWebRequest(
    @field:NotBlank
    @field:Size(max = 100)
    val title: String,

    @field:NotBlank
    @field:Size(max = 1000)
    val content: String,

    @field:NotNull
    val credit: Int,

    @field:NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    val activityDate: LocalDate
)