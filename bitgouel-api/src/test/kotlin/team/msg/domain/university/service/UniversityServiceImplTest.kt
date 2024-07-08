package team.msg.domain.university.service

import com.appmattus.kotlinfixture.kotlinFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.repository.findByIdOrNull
import team.msg.domain.university.exception.AlreadyExistUniversityException
import team.msg.domain.university.exception.UniversityHasProfessorConstraintException
import team.msg.domain.university.model.University
import team.msg.domain.university.presentation.data.request.CreateUniversityRequest
import team.msg.domain.university.repository.ProfessorRepository
import team.msg.domain.university.repository.UniversityRepository

class UniversityServiceImplTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    val fixture = kotlinFixture()

    val universityRepository = mockk<UniversityRepository>()
    val professorRepository = mockk<ProfessorRepository>()

    val universityServiceImpl = UniversityServiceImpl(
        universityRepository = universityRepository,
        professorRepository = professorRepository
    )

    // createUniversity 테스트 코드
    Given("CreateUniversityRequest 가 주어졌을 때") {
        val universityName = "대충 대학교"

        val request = fixture<CreateUniversityRequest> {
            property(University::name) { universityName }
        }

        val university = fixture<University> {
            property(University::name) { universityName }
        }

        every { universityRepository.existsByName(any()) } returns false
        every { universityRepository.save(any()) } returns university

        When("대학 추가 시") {
            universityServiceImpl.createUniversity(request)

            Then("University 가 저장이 되어야 한다") {
                verify(exactly = 1) { universityRepository.save(any()) }
            }
        }

        When("이미 존재하는 대학 이름이라면") {
            every { universityRepository.existsByName(any()) } returns true

            Then("AlreadyExistUniversityException 이 발생해야 한다") {
                shouldThrow<AlreadyExistUniversityException> {
                    universityServiceImpl.createUniversity(request)
                }
            }
        }
    }

    // deleteUniversity 테스트 코드
    Given("UniversityId 가 주어졌을 때") {
        val universityId = 1L

        val university = fixture<University>()

        every { universityRepository.findByIdOrNull(universityId) } returns university
        every { professorRepository.existsByUniversity(university) } returns false
        every { universityRepository.delete(university) } just Runs

        When("대학 삭제 시") {
            universityServiceImpl.deleteUniversity(universityId)

            Then("University 가 삭제가 되어야 한다") {
                verify(exactly = 1) { universityRepository.delete(university) }
            }
        }

        When("University 와 연관 관계를 가진 Professor 가 존재한다면") {
            every { professorRepository.existsByUniversity(university) } returns true

            Then("UniversityHasProfessorConstraintException 이 발생해야 한다") {
                shouldThrow<UniversityHasProfessorConstraintException> {
                    universityServiceImpl.deleteUniversity(universityId)
                }
            }
        }
    }
})