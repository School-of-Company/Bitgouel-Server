package team.msg.domain.post.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.msg.common.util.UserUtil
import team.msg.domain.post.model.Post
import team.msg.domain.post.presentation.data.request.CreatePostRequest
import team.msg.domain.post.repository.PostRepository
import team.msg.domain.post.enums.FeedType
import team.msg.domain.post.exception.ForbiddenPostException
import team.msg.domain.user.enums.Authority
import java.util.UUID

@Service
class PostServiceImpl(
    private val postRepository: PostRepository,
    private val userUtil: UserUtil
) : PostService {
    /**
     * 게시글을 생성하는 비지니스 로직입니다.
     * @param 게시글 생성에 필요한 정보가 담긴 dto
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun createPost(request: CreatePostRequest) {
        val user = userUtil.queryCurrentUser()

        when(user.authority){
            Authority.ROLE_COMPANY_INSTRUCTOR,
            Authority.ROLE_GOVERNMENT,
            Authority.ROLE_PROFESSOR,
            Authority.ROLE_BBOZZAK -> if (request.feedType == FeedType.INFORM) "공지를 작성할 권한이 없습니다." info user.authority
            else -> {}
        }

        val link = request.link ?: mutableListOf()

        val post = Post(
            id = UUID.randomUUID(),
            title = request.title,
            content = request.content,
            feedType = request.feedType,
            link = link,
            userId = user.id
        )

        postRepository.save(post)
    }

    infix fun String.info(authority: Authority) {
        throw ForbiddenPostException("$this info: [ userAuthority = $authority ]")
    }

}