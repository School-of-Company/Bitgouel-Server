package team.msg.domain.post.presentation

import javax.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import team.msg.domain.post.enums.FeedType
import team.msg.domain.post.mapper.PostRequestMapper
import team.msg.domain.post.presentation.web.CreatePostWebRequest
import team.msg.domain.post.service.PostService

@RestController
@RequestMapping("/post")
class PostController(
    private val postService: PostService,
    private val postRequestMapper: PostRequestMapper
) {
    @PostMapping
    fun createPost(@RequestBody @Valid webRequest: CreatePostWebRequest, @RequestParam(name = "type") feedType: FeedType){
        val request = postRequestMapper.createPostWebRequestToDto(webRequest, feedType)
        postService.createPostService(request)
    }
}