package team.msg.domain.student.service

import com.appmattus.kotlinfixture.kotlinFixture
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.mockk
import team.msg.common.util.UserUtil
import team.msg.domain.student.repository.StudentActivityRepository
import team.msg.domain.student.repository.StudentRepository
import team.msg.domain.teacher.repository.TeacherRepository

class StudentActivityServiceImplTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerLeaf
    val fixture = kotlinFixture()

    val userUtil = mockk<UserUtil>()
    val studentRepository = mockk<StudentRepository>()
    val teacherRepository = mockk<TeacherRepository>()
    val studentActivityRepository = mockk<StudentActivityRepository>()
    val studentActivityServiceImpl = StudentActivityServiceImpl(
        userUtil,
        studentRepository,
        teacherRepository,
        studentActivityRepository
    )
})