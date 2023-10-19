package team.msg.domain.lecture.presentation.data.web

import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.validation.constraints.Future
import javax.validation.constraints.FutureOrPresent
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import team.msg.domain.lecture.enum.LectureType
import java.time.LocalDateTime

data class LectureCreateWebRequest(
    @field:NotBlank
    val name: String,

    @field:NotBlank
    val content: String,

    @field:NotNull
    @FutureOrPresent
    val startDate: LocalDateTime,

    @field:NotNull
    @Future
    val endDate: LocalDateTime,

    @field:NotNull
    @Future
    val completeDate: LocalDateTime,

    @field:NotNull
    @Enumerated(EnumType.STRING)
    val lectureType: LectureType,

    @field:NotNull
    @field:Min(0)
    val credit: Int,

    @field:NotNull
    @field:Min(1)
    val maxRegisteredUser: Int
)
