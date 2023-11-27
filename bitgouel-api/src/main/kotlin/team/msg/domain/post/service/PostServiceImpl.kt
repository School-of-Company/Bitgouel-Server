package team.msg.domain.post.service

import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.msg.common.util.UserUtil
import team.msg.domain.post.model.Post
import team.msg.domain.post.presentation.data.request.CreatePostRequest
import team.msg.domain.post.repository.PostRepository
import team.msg.domain.post.enums.FeedType
import team.msg.domain.post.exception.ForbiddenPostException
import team.msg.domain.post.exception.PostNotFoundException
import team.msg.domain.post.presentation.data.response.PostDetailsResponse
import team.msg.domain.post.presentation.data.response.PostResponse
import team.msg.domain.post.presentation.data.response.PostsResponse
import team.msg.domain.user.enums.Authority
import team.msg.domain.user.exception.UserNotFoundException
import team.msg.domain.user.model.User
import team.msg.domain.user.repository.UserRepository
import java.util.UUID

@Service
class PostServiceImpl(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
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

    /**
     * 게시글 리스트를 조회하는 비지니스 로직입니다.
     * @param 가져올 게시글 유형과 게시글 리스트를 페이징 처리하기 위한 pageable
     * @return 페이징 처리된 게시글 리스트
     */
    @Transactional(readOnly = true)
    override fun queryPosts(type: FeedType, pageable: Pageable): PostsResponse {
        val posts = postRepository.findAllByFeedType(type, pageable)

        val response = PostResponse.pageOf(posts)

        return response
    }

    /**
     * 게시글을 상세조회하는 비지니스 로직입니다.
     * @param id 게시글을 상세 조회하기 위한 id
     * @return 상세조회한 게시글의 정보를 담은 dto
     */
    @Transactional(readOnly = true)
    override fun queryPostDetails(id: UUID): PostDetailsResponse {
        val post = postRepository findById id
        val user = userRepository findById post.userId

        val response = PostResponse.detailOf(post, user)

        return response
    }

    infix fun String.info(authority: Authority) {
        throw ForbiddenPostException("$this info: [ userAuthority = $authority ]")
    }

    private infix fun PostRepository.findById(id: UUID): Post = this.findByIdOrNull(id)
        ?: throw PostNotFoundException("게시글을 찾을 수 없습니다. info : [ postId = $id ]")


    private infix fun UserRepository.findById(id: UUID): User = this.findByIdOrNull(id)
        ?: throw UserNotFoundException("유저를 찾을 수 없습니다. info : [ userId = $id ]")

}