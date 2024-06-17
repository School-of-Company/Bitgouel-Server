package team.msg.domain.lecture.presentation.data.response
import org.springframework.data.domain.Page
import team.msg.domain.lecture.enums.LectureStatus
import team.msg.domain.lecture.enums.Semester
import team.msg.domain.lecture.model.Lecture
import team.msg.domain.lecture.model.LectureDate
import team.msg.domain.student.model.Student
import team.msg.domain.user.enums.Authority
import team.msg.domain.user.model.User
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

data class LectureResponse(
    val id: UUID,
    val name: String,
    val content: String,
    val semester: Semester,
    val division: String,
    val department: String,
    val line: String,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val lectureType: String,
    val lectureStatus: LectureStatus,
    val headCount: Int,
    val maxRegisteredUser: Int,
    val lecturer: String,
    val essentialComplete: Boolean
) {
    companion object {
        fun of(lecture: Lecture, headCount: Int): LectureResponse = LectureResponse(
            id = lecture.id,
            name = lecture.name,
            content = lecture.content,
            semester = lecture.semester,
            division = lecture.division,
            department = lecture.department,
            line = lecture.line,
            startDate = lecture.startDate,
            endDate = lecture.endDate,
            lectureType = lecture.lectureType,
            lectureStatus = lecture.getLectureStatus(),
            headCount = headCount,
            maxRegisteredUser = lecture.maxRegisteredUser,
            lecturer = lecture.instructor,
            essentialComplete = lecture.essentialComplete
        )

        fun detailOf(lecture: Lecture, headCount: Int, isRegistered: Boolean, lectureDates: List<LectureDate>): LectureDetailsResponse = LectureDetailsResponse(
            name = lecture.name,
            content = lecture.content,
            semester = lecture.semester,
            division = lecture.division,
            department = lecture.department,
            line = lecture.line,
            createAt = lecture.createdAt,
            startDate = lecture.startDate,
            endDate = lecture.endDate,
            lectureDates = dateListOf(lectureDates),
            lectureType = lecture.lectureType,
            lectureStatus = lecture.getLectureStatus(),
            headCount = headCount,
            maxRegisteredUser = lecture.maxRegisteredUser,
            isRegistered = isRegistered,
            lecturer = lecture.instructor,
            credit = lecture.credit,
            essentialComplete = lecture.essentialComplete
        )

        fun dateListOf(lectureDates: List<LectureDate>): List<LectureDateResponse> =
            lectureDates.map { dateOf(it) }

        fun dateOf(lectureDate: LectureDate): LectureDateResponse = LectureDateResponse(
            completeDate = lectureDate.completeDate,
            startTime = lectureDate.startTime,
            endTime = lectureDate.endTime
        )

        fun instructorOf(user: User, organization: String): InstructorResponse = InstructorResponse(
            id = user.id,
            name = user.name,
            organization = organization,
            authority = user.authority
        )

        fun lineOf(lines: List<String>): LinesResponse = LinesResponse(
            lines = lines
        )

        fun departmentOf(departments: List<String>): DepartmentsResponse = DepartmentsResponse(
            departments = departments
        )
        
        fun of(lecture: Lecture, isComplete: Boolean, currentCompletedDate: LocalDate?) = SignedUpLectureResponse(
            id = lecture.id,
            name = lecture.name,
            lectureType = lecture.lectureType,
            currentCompletedDate = currentCompletedDate,
            lecturer = lecture.instructor,
            isComplete = isComplete
        )

        fun signedUpOf(lectures: List<SignedUpLectureResponse>) = SignedUpLecturesResponse(
            lectures = lectures
        )

        fun of(student: Student, isComplete: Boolean) = student.run {
            SignedUpStudentResponse(
                id = id,
                email = user!!.email,
                name = user!!.name,
                grade = grade,
                classNumber = classRoom,
                number = number,
                phoneNumber = user!!.phoneNumber,
                school = club.school.highSchool,
                clubName = club.name,
                cohort = cohort,
                isComplete = isComplete
            )
        }

        fun signedUpOf(students: List<SignedUpStudentResponse>) = SignedUpStudentsResponse(
            students = students
        )
        
        fun divisionOf(divisions: List<String>): DivisionsResponse = DivisionsResponse(
            divisions = divisions
        )
    }
}

data class LecturesResponse(
    val lectures: Page<LectureResponse>
)

data class LectureDetailsResponse(
    val name: String,
    val content: String,
    val semester: Semester,
    val division: String,
    val department: String,
    val line: String,
    val createAt: LocalDateTime,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val lectureDates: List<LectureDateResponse>,
    val lectureType: String,
    val lectureStatus: LectureStatus,
    val headCount: Int,
    val maxRegisteredUser: Int,
    val isRegistered: Boolean,
    val lecturer: String,
    val credit: Int,
    val essentialComplete: Boolean
)

data class LectureDateResponse(
    val completeDate: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime
)

data class InstructorsResponse(
    val instructors: List<InstructorResponse>
)

data class InstructorResponse(
    val id: UUID,
    val name: String,
    val organization: String,
    val authority: Authority
)

data class LinesResponse(
    val lines: List<String>
)

data class DepartmentsResponse(
    val departments: List<String>
)

data class SignedUpLecturesResponse(
    val lectures: List<SignedUpLectureResponse>
)

data class SignedUpLectureResponse(
    val id: UUID,
    val name: String,
    val lectureType: String,
    val currentCompletedDate: LocalDate?,
    val lecturer: String,
    val isComplete: Boolean
)

data class SignedUpStudentsResponse(
    val students: List<SignedUpStudentResponse>
)

data class SignedUpStudentResponse(
    val id: UUID,
    val email: String,
    val name: String,
    val grade: Int,
    val classNumber: Int,
    val number: Int,
    val phoneNumber: String,
    val school: String,
    val clubName: String,
    val cohort: Int,
    val isComplete: Boolean
)

data class DivisionsResponse(
    val divisions: List<String>
)