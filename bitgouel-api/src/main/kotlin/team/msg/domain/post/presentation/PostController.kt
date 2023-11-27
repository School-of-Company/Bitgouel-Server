package team.msg.domain.post.presentation

import javax.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import team.msg.domain.post.enums.FeedType
import team.msg.domain.post.mapper.PostRequestMapper
import team.msg.domain.post.presentation.data.response.PostsResponse
import team.msg.domain.post.presentation.web.CreatePostWebRequest
import team.msg.domain.post.service.PostService

@RestController
@RequestMapping("/post")
class PostController(
    private val postService: PostService,
    private val postRequestMapper: PostRequestMapper
) {
    @PostMapping
    fun createPost(@RequestBody @Valid webRequest: CreatePostWebRequest): ResponseEntity<Void> {
        val request = postRequestMapper.createPostWebRequestToDto(webRequest)
        postService.createPost(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @GetMapping
    fun queryPosts(@RequestParam type: FeedType, pageable: Pageable): ResponseEntity<PostsResponse> {
        val response = postService.queryPosts(type, pageable)
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }
}