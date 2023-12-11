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
import team.msg.domain.post.presentation.data.request.UpdatePostRequest
import team.msg.domain.post.presentation.data.response.PostDetailsResponse
import team.msg.domain.post.presentation.data.response.PostResponse
import team.msg.domain.post.presentation.data.response.PostsResponse
import team.msg.domain.user.enums.Authority
import team.msg.domain.user.exception.UserNotFoundException
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

        if(request.feedType == FeedType.NOTICE && user.authority != Authority.ROLE_ADMIN)
           throw ForbiddenPostException("공지를 작성할 권한이 없습니다. info: [ userAuthority = ${user.authority} ]")

        val post = request.run {
            Post(
                id = UUID.randomUUID(),
                userId = user.id,
                feedType = feedType,
                title = title,
                content = content,
                links = links
            )
        }

        postRepository.save(post)
    }

    /**
     * 게시글을 수정하는 비지니스 로직입니다.
     * @param 게시글 id, 게시글을 수정하기 위한 데이터들을 담은 request Dto
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun updatePost(id: UUID, request: UpdatePostRequest) {
        val user = userUtil.queryCurrentUser()

        val post = postRepository findById id

        if(user.id != post.userId)
            throw ForbiddenPostException("게시글은 본인만 수정할 수 있습니다. info : [ userId = ${user.id} ]")

        val updatePost = Post(
            id = post.id,
            userId = post.userId,
            feedType = post.feedType,
            title = request.title,
            content = request.content,
            links = request.links,
        )

        postRepository.save(updatePost)
    }

    /**
     * 게시글을 삭제하는 비지니스 로직입니다.
     * @param 게시글을 삭제하기 위한 게시글 id
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun deletePost(id: UUID) {
        val user = userUtil.queryCurrentUser()

        val post = postRepository findById id

        if(user.id != post.userId && user.authority != Authority.ROLE_ADMIN)
           throw ForbiddenPostException("게시글은 작성자 또는 관리자만 삭제할 수 있습니다. info : [ userId = ${user.id} ]")

        postRepository.delete(post)
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
     * @param 게시글을 상세 조회하기 위한 게시글 id
     * @return 상세조회한 게시글의 정보를 담은 dto
     */
    @Transactional(readOnly = true)
    override fun queryPostDetails(id: UUID): PostDetailsResponse {
        val post = postRepository findById id
        val writer = userRepository findNameById post.userId

        val response = PostResponse.detailOf(post, writer)

        return response
    }

    private infix fun PostRepository.findById(id: UUID): Post = this.findByIdOrNull(id)
        ?: throw PostNotFoundException("게시글을 찾을 수 없습니다. info : [ postId = $id ]")


    private infix fun UserRepository.findNameById(id: UUID): String = this.queryNameById(id)?.name
        ?: throw UserNotFoundException("유저를 찾을 수 없습니다. info : [ userId = $id ]")

}