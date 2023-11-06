package team.msg.domain.lecture.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import team.msg.common.enums.ApproveStatus
import team.msg.domain.lecture.enums.LectureType
import team.msg.domain.lecture.model.Lecture
import java.util.*


interface LectureRepository : JpaRepository<Lecture, UUID> {
    @EntityGraph(attributePaths = ["user"])
    @Query("SELECT l FROM Lecture l " +
            "WHERE (:approve_status is null or l.approveStatus = :approve_status) " +
            "AND (:lecture_type is null or l.lectureType = :lecture_type)")
    fun findAllByApproveStatusAndLectureType(pageable: Pageable, @Param("approve_status") approveStatus: ApproveStatus?, @Param("lecture_type") lectureType: LectureType?): Page<Lecture>
}