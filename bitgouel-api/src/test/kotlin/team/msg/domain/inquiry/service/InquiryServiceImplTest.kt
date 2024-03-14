package team.msg.domain.inquiry.service

import com.appmattus.kotlinfixture.kotlinFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.repository.findByIdOrNull
import team.msg.common.util.UserUtil
import team.msg.domain.inquiry.enums.AnswerStatus
import team.msg.domain.inquiry.exception.AlreadyAnsweredInquiryException
import team.msg.domain.inquiry.exception.ForbiddenCommandInquiryException
import team.msg.domain.inquiry.exception.InquiryAnswerNotFoundException
import team.msg.domain.inquiry.exception.InquiryNotFoundException
import team.msg.domain.inquiry.model.Inquiry
import team.msg.domain.inquiry.model.InquiryAnswer
import team.msg.domain.inquiry.presentation.request.CreateInquiryAnswerRequest
import team.msg.domain.inquiry.presentation.request.CreateInquiryRequest
import team.msg.domain.inquiry.presentation.request.UpdateInquiryRequest
import team.msg.domain.inquiry.presentation.response.InquiryDetailResponse
import team.msg.domain.inquiry.presentation.response.InquiryResponse
import team.msg.domain.inquiry.presentation.response.InquiryResponses
import team.msg.domain.inquiry.presentation.web.QueryAllInquiresRequest
import team.msg.domain.inquiry.repository.InquiryAnswerRepository
import team.msg.domain.inquiry.repository.InquiryRepository
import team.msg.domain.user.enums.Authority
import team.msg.domain.user.model.User
import java.util.*

class InquiryServiceImplTest : BehaviorSpec ({

    isolationMode = IsolationMode.InstancePerLeaf
    val fixture = kotlinFixture()

    val userUtil = mockk<UserUtil>()
    val inquiryRepository = mockk<InquiryRepository>()
    val inquiryAnswerRepository = mockk<InquiryAnswerRepository>()
    val inquiryServiceImpl = InquiryServiceImpl(
        userUtil,
        inquiryRepository,
        inquiryAnswerRepository
    )

    // createInquiry 테스트 코드
    Given("CreateInquiryRequest 가 주어질 때") {
        val user = fixture<User>()
        val inquiry = fixture<Inquiry>()
        val request = fixture<CreateInquiryRequest>()

        every { userUtil.queryCurrentUser() } returns user
        every { inquiryRepository.save(any()) } returns inquiry

        When("문의사항 등록 요청을 하면") {
            inquiryServiceImpl.createInquiry(request)

            Then("Inquiry 이 저장이 되어야 한다.") {
                verify(exactly = 1) { inquiryRepository.save(any()) }
            }
        }
    }

    // queryMyInquiries 테스트 코드
    Given("Inquiry 가 주어질 때") {
        val user = fixture<User>()
        val inquiry = fixture<Inquiry>()
        val inquiryResponse = fixture<InquiryResponse> {
            property(InquiryResponse::id) { inquiry.id }
            property(InquiryResponse::question) { inquiry.question }
            property(InquiryResponse::userId) { inquiry.user.id }
            property(InquiryResponse::username) { inquiry.user.name }
            property(InquiryResponse::createdAt) { inquiry.createdAt.toLocalDate() }
            property(InquiryResponse::answerStatus) { inquiry.answerStatus }
        }
        val response = fixture<InquiryResponses> {
            property(InquiryResponses::inquiries) { listOf(inquiryResponse) }
        }

        every { userUtil.queryCurrentUser() } returns user
        every { inquiryRepository.findAllByUser(user) } returns listOf(inquiry)

        When("문의사항 전체 조회 요청을 하면") {
            val result = inquiryServiceImpl.queryMyInquiries()

            Then("result와 response가 같아야 한다.") {
                result shouldBe response
            }
        }
    }

    // queryAllInquiries 테스트 코드
    Given("QueryAllInquiresRequest 가 주어질 때") {
        val request = fixture<QueryAllInquiresRequest>()
        val keywordRequest = fixture<QueryAllInquiresRequest> {
            property(QueryAllInquiresRequest::keyword) { "keyword" }
        }
        val answerStatusRequest = fixture<QueryAllInquiresRequest> {
            property(QueryAllInquiresRequest::answerStatus) { AnswerStatus.ANSWERED }
        }

        val inquiry = fixture<Inquiry>()
        val inquiryResponse = fixture<InquiryResponse> {
            property(InquiryResponse::id) { inquiry.id }
            property(InquiryResponse::question) { inquiry.question }
            property(InquiryResponse::userId) { inquiry.user.id }
            property(InquiryResponse::username) { inquiry.user.name }
            property(InquiryResponse::createdAt) { inquiry.createdAt.toLocalDate() }
            property(InquiryResponse::answerStatus) { inquiry.answerStatus }
        }
        val response = fixture<InquiryResponses> {
            property(InquiryResponses::inquiries) { listOf(inquiryResponse) }
        }

        every { inquiryRepository.search(any(), any()) } returns listOf(inquiry)

        When("문의사항 전체 조회 요청을 하면") {
            val result = inquiryServiceImpl.queryAllInquiries(request)

            Then("result와 response가 같아야 한다.") {
                result shouldBe response
            }
        }

        When("keyword 로 문의사항 전체 조회 요청을 하면") {
            val result = inquiryServiceImpl.queryAllInquiries(keywordRequest)

            Then("result와 response가 같아야 한다.") {
                result shouldBe response
            }
        }

        When("답변 여부로 문의사항 전체 조회 요청을 하면") {
            val result = inquiryServiceImpl.queryAllInquiries(answerStatusRequest)

            Then("result와 response가 같아야 한다.") {
                result shouldBe response
            }
        }
    }

    // queryInquiryDetail 테스트 코드
    Given("inquiry id 가 주어질 때") {
        val inquiryId = UUID.randomUUID()

        val user = fixture<User> {
            property(User::authority) { Authority.ROLE_ADMIN }
        }
        val invalidUser = fixture<User> {
            property(User::authority) { Authority.ROLE_STUDENT }
        }
        val inquiry = fixture<Inquiry> {
            property(Inquiry::id) { inquiryId }
            property(Inquiry::user) { user }
        }
        val inquiryAnswer = fixture<InquiryAnswer> {
            property(InquiryAnswer::admin) { user }
            property(InquiryAnswer::answer) { "answer" }
        }
        val response = fixture<InquiryDetailResponse> {
            property(InquiryDetailResponse::id) { inquiry.id }
            property(InquiryDetailResponse::question) { inquiry.question }
            property(InquiryDetailResponse::questionDetail) { inquiry.questionDetail }
            property(InquiryDetailResponse::questionerId) { inquiry.user.id }
            property(InquiryDetailResponse::questioner) { inquiry.user.name }
            property(InquiryDetailResponse::questionDate) { inquiry.createdAt }
            property(InquiryDetailResponse::answerStatus) { inquiry.answerStatus }
            property(InquiryDetailResponse::answer) { inquiryAnswer.answer }
            property(InquiryDetailResponse::adminId) { inquiryAnswer.admin.id }
            property(InquiryDetailResponse::answeredDate) { inquiryAnswer.createdAt }
        }

        every { userUtil.queryCurrentUser() } returns user
        every { inquiryRepository.findByIdOrNull(inquiryId) } returns inquiry
        every { inquiryAnswerRepository.findByInquiryId(any()) } returns inquiryAnswer

        When("문의사항 상세 조회 요청을 하면") {
            val result = inquiryServiceImpl.queryInquiryDetail(inquiryId)

            Then("result와 response가 같아야 한다.") {
                result shouldBe response
            }
        }

        When("inquiry id 에 맞는 Inquiry 가 없다면") {
            every { inquiryRepository.findByIdOrNull(inquiryId) } returns null

            Then("InquiryNotFoundException 이 발생해야 한다.") {
                shouldThrow<InquiryNotFoundException> {
                    inquiryServiceImpl.queryInquiryDetail(inquiryId)
                }
            }
        }

        When("어드민이 아니라면") {
            every { userUtil.queryCurrentUser() } returns invalidUser

            Then("ForbiddenCommandInquiryException 이 발생해야 한다.") {
                shouldThrow<ForbiddenCommandInquiryException> {
                    inquiryServiceImpl.queryInquiryDetail(inquiryId)
                }
            }
        }
    }

    // deleteInquiry 테스트 코드
    Given("inquiry id 가 주어질 때") {
        val inquiryId = UUID.randomUUID()

        val user = fixture<User> {
            property(User::authority) { Authority.ROLE_ADMIN }
        }
        val invalidUser = fixture<User> {
            property(User::authority) { Authority.ROLE_STUDENT }
        }
        val inquiry = fixture<Inquiry> {
            property(Inquiry::id) { inquiryId }
            property(Inquiry::user) { user }
            property(Inquiry::answerStatus) { AnswerStatus.ANSWERED }
        }
        val inquiryAnswer = fixture<InquiryAnswer> {
            property(InquiryAnswer::admin) { user }
            property(InquiryAnswer::answer) { "answer" }
        }

        every { userUtil.queryCurrentUser() } returns user
        every { inquiryRepository.findByIdOrNull(any()) } returns inquiry
        every { inquiryAnswerRepository.findByInquiryId(any()) } returns inquiryAnswer
        every { inquiryAnswerRepository.delete(inquiryAnswer) } returns Unit
        every { inquiryRepository.deleteById(inquiryId) } returns Unit

        When("답변된 문의사항 삭제 요청을 하면") {
            inquiryServiceImpl.deleteInquiry(inquiryId)

            Then("Inquiry 가 삭제되어야 한다.") {
                verify(exactly = 1) { inquiryRepository.deleteById(inquiryId) }
            }

            Then("Inquiry Answer 가 삭제되어야 한다.") {
                verify(exactly = 1) { inquiryAnswerRepository.delete(inquiryAnswer) }
            }
        }

        When("답변이 안된 문의사항 삭제 요청을 하면") {
            every { inquiryAnswerRepository.findByInquiryId(any()) } returns null

            Then("InquiryAnswerNotFoundException 가 발생해야 한다.") {
                shouldThrow<InquiryAnswerNotFoundException> {
                    inquiryServiceImpl.deleteInquiry(inquiryId)
                }
            }
        }

        When("inquiry id 에 맞는 Inquiry 가 없다면") {
            every { inquiryRepository.findByIdOrNull(any()) } returns null

            Then("InquiryNotFoundException 이 발생해야 한다.") {
                shouldThrow<InquiryNotFoundException> {
                    inquiryServiceImpl.deleteInquiry(inquiryId)
                }
            }
        }

        When("유저가 작성자가 아니라면") {
            every { userUtil.queryCurrentUser() } returns invalidUser

            Then("ForbiddenCommandInquiryException 이 발생해야 한다.") {
                shouldThrow<ForbiddenCommandInquiryException> {
                    inquiryServiceImpl.deleteInquiry(inquiryId)
                }
            }
        }
    }

    // rejectInquiry 테스트 코드
    Given("inquiry id 가 주어질 때") {
        val inquiryId = UUID.randomUUID()

        val user = fixture<User> {
            property(User::authority) { Authority.ROLE_ADMIN }
        }
        val inquiry = fixture<Inquiry> {
            property(Inquiry::id) { inquiryId }
            property(Inquiry::user) { user }
        }
        val inquiryAnswer = fixture<InquiryAnswer> {
            property(InquiryAnswer::admin) { user }
            property(InquiryAnswer::answer) { "answer" }
        }

        every { inquiryRepository.findByIdOrNull(any()) } returns inquiry
        every { inquiryAnswerRepository.findByInquiryId(any()) } returns inquiryAnswer
        every { inquiryAnswerRepository.delete(inquiryAnswer) } returns Unit
        every { inquiryRepository.deleteById(inquiryId) } returns Unit

        When("문의사항 삭제 요청을 하면") {
            inquiryServiceImpl.rejectInquiry(inquiryId)

            Then("Inquiry 가 삭제되어야 한다.") {
                verify(exactly = 1) { inquiryRepository.deleteById(inquiryId) }
            }
        }

        When("inquiry id 에 맞는 Inquiry 가 없다면") {
            every { inquiryRepository.findByIdOrNull(any()) } returns null

            Then("InquiryNotFoundException 이 발생해야 한다.") {
                shouldThrow<InquiryNotFoundException> {
                    inquiryServiceImpl.rejectInquiry(inquiryId)
                }
            }
        }
    }

    // updateInquiry 테스트 코드
    Given("inquiry id 와 UpdateInquiryRequest 가 주어질 때") {
        val inquiryId = UUID.randomUUID()
        val request = fixture<UpdateInquiryRequest>()

        val user = fixture<User> {
            property(User::authority) { Authority.ROLE_ADMIN }
        }
        val invalidUser = fixture<User> {
            property(User::authority) { Authority.ROLE_STUDENT }
        }
        val inquiry = fixture<Inquiry> {
            property(Inquiry::id) { inquiryId }
            property(Inquiry::user) { user }
        }

        every { userUtil.queryCurrentUser() } returns user
        every { inquiryRepository.findByIdOrNull(any()) } returns inquiry
        every { inquiryRepository.save(any()) } returns inquiry

        When("문의사항 수정 요청을 하면") {
            inquiryServiceImpl.updateInquiry(inquiryId, request)

            Then("Inquiry 가 수정되어야 한다.") {
                verify(exactly = 1) { inquiryRepository.save(any()) }
            }
        }

        When("inquiry id 에 맞는 Inquiry 가 없다면") {
            every { inquiryRepository.findByIdOrNull(any()) } returns null

            Then("InquiryNotFoundException 이 발생해야 한다.") {
                shouldThrow<InquiryNotFoundException> {
                    inquiryServiceImpl.updateInquiry(inquiryId, request)
                }
            }
        }

        When("유저가 작성자가 아니라면") {
            every { userUtil.queryCurrentUser() } returns invalidUser

            Then("ForbiddenCommandInquiryException 이 발생해야 한다.") {
                shouldThrow<ForbiddenCommandInquiryException> {
                    inquiryServiceImpl.updateInquiry(inquiryId, request)
                }
            }
        }
    }

    // replyInquiry 테스트 코드
    Given("inquiry id 와 CreateInquiryAnswerRequest 가 주어질 때") {
        val inquiryId = UUID.randomUUID()
        val request = fixture<CreateInquiryAnswerRequest>()

        val user = fixture<User> {
            property(User::authority) { Authority.ROLE_ADMIN }
        }
        val inquiry = fixture<Inquiry> {
            property(Inquiry::id) { inquiryId }
            property(Inquiry::user) { user }
        }
        val inquiryAnswer = fixture<InquiryAnswer> {
            property(InquiryAnswer::admin) { user }
            property(InquiryAnswer::answer) { "answer" }
        }

        every { userUtil.queryCurrentUser() } returns user
        every { inquiryRepository.findByIdOrNull(any()) } returns inquiry
        every { inquiryAnswerRepository.save(any()) } returns inquiryAnswer
        every { inquiryAnswerRepository.existsByInquiryId(inquiryId) } returns false

        When("문의사항 삭제 요청을 하면") {
            inquiryServiceImpl.replyInquiry(inquiryId, request)

            Then("Inquiry 가 삭제되어야 한다.") {
                verify(exactly = 1) { inquiryAnswerRepository.save(any()) }
            }
        }

        When("inquiry id 에 맞는 Inquiry 가 없다면") {
            every { inquiryRepository.findByIdOrNull(any()) } returns null

            Then("InquiryNotFoundException 이 발생해야 한다.") {
                shouldThrow<InquiryNotFoundException> {
                    inquiryServiceImpl.replyInquiry(inquiryId, request)
                }
            }
        }

        When("inquiry에 이미 답변이 존재할 경우") {
            every { inquiryAnswerRepository.existsByInquiryId(inquiryId) } returns true

            Then("AlreadyAnsweredInquiryException 이 발생해야 한다.") {
                shouldThrow<AlreadyAnsweredInquiryException> {
                    inquiryServiceImpl.replyInquiry(inquiryId, request)
                }
            }
        }
    }
})