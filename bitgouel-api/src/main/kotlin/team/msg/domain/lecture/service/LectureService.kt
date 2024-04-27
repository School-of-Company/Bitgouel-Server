package team.msg.domain.lecture.service

import javax.servlet.http.HttpServletResponse
import org.springframework.data.domain.Pageable
import team.msg.domain.lecture.presentation.data.request.CreateLectureRequest
import team.msg.domain.lecture.presentation.data.request.QueryAllDepartmentsRequest
import team.msg.domain.lecture.presentation.data.request.QueryAllDivisionsRequest
import team.msg.domain.lecture.presentation.data.request.QueryAllLectureRequest
import team.msg.domain.lecture.presentation.data.request.QueryAllLinesRequest
import team.msg.domain.lecture.presentation.data.response.CompletedLecturesResponse
import team.msg.domain.lecture.presentation.data.response.DepartmentsResponse
import team.msg.domain.lecture.presentation.data.response.DivisionsResponse
import team.msg.domain.lecture.presentation.data.response.InstructorsResponse
import team.msg.domain.lecture.presentation.data.response.LecturesResponse
import team.msg.domain.lecture.presentation.data.response.LectureDetailsResponse
import team.msg.domain.lecture.presentation.data.response.LinesResponse
import java.util.UUID

interface LectureService {
    fun createLecture(request: CreateLectureRequest)
    fun queryAllLectures(pageable: Pageable, queryAllLectureRequest: QueryAllLectureRequest): LecturesResponse
    fun queryLectureDetails(id: UUID): LectureDetailsResponse
    fun queryAllLines(request: QueryAllLinesRequest): LinesResponse
    fun queryAllDepartments(request: QueryAllDepartmentsRequest): DepartmentsResponse
    fun queryAllDivisions(request: QueryAllDivisionsRequest): DivisionsResponse
    fun signUpLecture(id: UUID)
    fun cancelSignUpLecture(id: UUID)
    fun queryInstructors(keyword: String): InstructorsResponse
    fun queryAllCompletedLecturesByStudent(studentId: UUID): CompletedLecturesResponse
    fun lectureReceiptStatusExcel(response: HttpServletResponse)
}