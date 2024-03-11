package team.msg.domain.lecture.presentation.data.web

import com.fasterxml.jackson.annotation.JsonFormat
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.validation.constraints.Future
import javax.validation.constraints.FutureOrPresent
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import team.msg.domain.lecture.enums.Division
import team.msg.domain.lecture.enums.LectureType
import team.msg.domain.lecture.enums.Semester
import java.time.LocalDateTime
import java.util.UUID

data class CreateLectureWebRequest(
    @field:NotBlank
    val name: String,

    @field:NotBlank
    val content: String,

    @field:NotNull
    @Enumerated(EnumType.STRING)
    val semester: Semester,

    @field:NotNull
    @Enumerated(EnumType.STRING)
    val division: Division,

    @field:NotBlank
    val department: String,

    @field:NotBlank
    val line: String,

    @field:NotBlank
    val userId: UUID,

    @field:NotNull
    @FutureOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val startDate: LocalDateTime,

    @field:NotNull
    @Future
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val endDate: LocalDateTime,

    @field:NotNull
    @Future
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val completeDate: LocalDateTime,

    @field:NotNull
    @Enumerated(EnumType.STRING)
    val lectureType: LectureType,

    @field:NotNull
    @field:Min(0)
    val credit: Int,

    @field:NotNull
    @field:Min(5)
    @field:Max(10)
    val maxRegisteredUser: Int
)