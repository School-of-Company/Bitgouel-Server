package team.msg.domain.lecture.service

import javax.servlet.http.HttpServletResponse
import org.springframework.data.domain.Pageable
import team.msg.domain.lecture.presentation.data.request.CreateLectureRequest
import team.msg.domain.lecture.presentation.data.request.QueryAllDepartmentsRequest
import team.msg.domain.lecture.presentation.data.request.QueryAllDivisionsRequest
import team.msg.domain.lecture.presentation.data.request.QueryAllLectureRequest
import team.msg.domain.lecture.presentation.data.request.QueryAllLinesRequest
import team.msg.domain.lecture.presentation.data.request.UpdateLectureRequest
import team.msg.domain.lecture.presentation.data.response.SignedUpLecturesResponse
import team.msg.domain.lecture.presentation.data.response.DepartmentsResponse
import team.msg.domain.lecture.presentation.data.response.DivisionsResponse
import team.msg.domain.lecture.presentation.data.response.InstructorsResponse
import team.msg.domain.lecture.presentation.data.response.LecturesResponse
import team.msg.domain.lecture.presentation.data.response.LectureDetailsResponse
import team.msg.domain.lecture.presentation.data.response.LinesResponse
import team.msg.domain.lecture.presentation.data.response.SignedUpStudentDetailsResponse
import team.msg.domain.lecture.presentation.data.response.SignedUpStudentsResponse
import java.util.UUID

interface LectureService {
    fun createLecture(request: CreateLectureRequest)
    fun updateLecture(id: UUID, request: UpdateLectureRequest)
    fun deleteLecture(id: UUID)
    fun queryAllLectures(pageable: Pageable, request: QueryAllLectureRequest): LecturesResponse
    fun queryLectureDetails(id: UUID): LectureDetailsResponse
    fun queryAllLines(request: QueryAllLinesRequest): LinesResponse
    fun queryAllDepartments(request: QueryAllDepartmentsRequest): DepartmentsResponse
    fun queryAllDivisions(request: QueryAllDivisionsRequest): DivisionsResponse
    fun signUpLecture(id: UUID)
    fun cancelSignUpLecture(id: UUID)
    fun queryInstructors(keyword: String): InstructorsResponse
    fun queryAllSignedUpLectures(studentId: UUID): SignedUpLecturesResponse
    fun queryAllSignedUpStudents(id: UUID): SignedUpStudentsResponse
    fun querySignedUpStudentDetails(id: UUID, studentId: UUID): SignedUpStudentDetailsResponse
    fun updateLectureCompleteStatus(id: UUID, studentIds: List<UUID>)
    fun cancelLectureCompleteStatus(id: UUID, studentIds: List<UUID>)
    fun lectureReceiptStatusExcel(response: HttpServletResponse)
}