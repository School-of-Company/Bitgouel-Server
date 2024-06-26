package team.msg.domain.post.presentation

import javax.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import team.msg.domain.post.enums.FeedType
import team.msg.domain.post.mapper.PostRequestMapper
import team.msg.domain.post.presentation.data.response.PagingPostsResponse
import team.msg.domain.post.presentation.data.response.PostDetailsResponse
import team.msg.domain.post.presentation.data.response.PostsResponse
import team.msg.domain.post.presentation.web.CreatePostWebRequest
import team.msg.domain.post.presentation.web.QueryAllPostsWebRequest
import team.msg.domain.post.presentation.web.UpdatePostWebRequest
import team.msg.domain.post.service.PostService
import java.util.*

@RestController
@RequestMapping("/post")
class PostController(
    private val postService: PostService,
    private val postRequestMapper: PostRequestMapper
) {
    @PostMapping
    fun createPost(@RequestBody @Valid webRequest: CreatePostWebRequest): ResponseEntity<Unit> {
        val request = postRequestMapper.createPostWebRequestToDto(webRequest)
        postService.createPost(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @GetMapping
    fun queryPosts(@RequestParam type: FeedType, pageable: Pageable): ResponseEntity<PagingPostsResponse> {
        val response = postService.queryPosts(type, pageable)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/all")
    fun queryPosts(webRequest: QueryAllPostsWebRequest): ResponseEntity<PostsResponse> {
        val request = postRequestMapper.queryAllPostsWebRequestToDto(webRequest)
        val response = postService.queryPosts(request)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{id}")
    fun queryPostDetails(@PathVariable id: UUID): ResponseEntity<PostDetailsResponse> {
        val response = postService.queryPostDetails(id)
        return ResponseEntity.ok(response)
    }

    @PatchMapping("/{id}")
    fun updatePost(@PathVariable id: UUID, @RequestBody @Valid webRequest: UpdatePostWebRequest): ResponseEntity<Unit> {
        val request = postRequestMapper.updatePostWebRequestToDto(webRequest)
        postService.updatePost(id, request)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/{id}")
    fun deletePost(@PathVariable id: UUID): ResponseEntity<Unit> {
        postService.deletePost(id)
        return ResponseEntity.noContent().build()
    }
}