package team.msg.domain.lecture.repository.custom

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import team.msg.domain.lecture.model.Lecture
import java.util.UUID

interface CustomLectureRepository {
    fun findAllByLectureType(pageable: Pageable,lectureType: String?): Page<Lecture>
    fun deleteAllByUserId(userId: UUID)
    fun findAllLineByDivision(division: String, keyword: String?): List<String>
    fun findAllDepartment(keyword: String?): List<String>
    fun findAllDivisions(keyword: String?): List<String>
}