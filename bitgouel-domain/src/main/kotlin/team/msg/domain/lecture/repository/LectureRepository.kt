package team.msg.domain.lecture.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import team.msg.domain.lecture.model.Lecture
import team.msg.domain.lecture.repository.custom.CustomLectureRepository
import team.msg.domain.user.model.User
import java.util.*


interface LectureRepository : JpaRepository<Lecture, UUID>, CustomLectureRepository {
    @Query("select l from Lecture l where l.id = :id and l.isDeleted = false ")
    override fun findById(id: UUID): Optional<Lecture>

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Lecture l set l.user = null where l.user = :user")
    fun updateAllByUser(user: User)
}