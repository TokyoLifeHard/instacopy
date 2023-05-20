package com.instacopy.instacopy.web;

import com.instacopy.instacopy.dto.PostDTO;
import com.instacopy.instacopy.entity.Post;
import com.instacopy.instacopy.facade.PostFacade;
import com.instacopy.instacopy.payload.response.MessageResponse;
import com.instacopy.instacopy.service.PostService;
import com.instacopy.instacopy.validations.ResponseErrorValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/post")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private PostFacade postFacade;
    @Autowired
    private ResponseErrorValidation resposeErrorValidation;

    @PostMapping("/create")
    public ResponseEntity<Object> createPost(@Valid @RequestBody PostDTO postDTO,
                                             BindingResult bindingResult,
                                             Principal principal){

        ResponseEntity<Object> errors = resposeErrorValidation.mapValidationService(bindingResult);
        if(!ObjectUtils.isEmpty(errors)) return errors;

        Post post = postService.createPost(postDTO, principal);
        PostDTO createdPost = postFacade.postToPostDTO(post);

        return new ResponseEntity<>(createdPost, HttpStatus.OK);
    }
    @GetMapping("/all")
    public ResponseEntity<List<PostDTO>> getAllPosts(){
        List<PostDTO> postDTOS = postService.getAllPosts()
                .stream()
                .map(postFacade::postToPostDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(postDTOS,HttpStatus.OK);
    }
    @GetMapping("/user/posts")
    public ResponseEntity<List<PostDTO>> getAllPostsForUser(Principal principal){
        List<PostDTO> postDTOS = postService.getAllUsersPosts(principal)
                .stream()
                .map(postFacade::postToPostDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(postDTOS,HttpStatus.OK);
    }

    @PostMapping("/{postId}/{username}/like")
    public ResponseEntity<PostDTO> likePost(@PathVariable("postId") String postId,
                                            @PathVariable("username") String username){

        Post post = postService.likePost(Long.parseLong(postId), username);
        PostDTO postDTO = postFacade.postToPostDTO(post);

        return new ResponseEntity<>(postDTO,HttpStatus.OK);
    }

    @PostMapping("/{postId}/delete")
    public ResponseEntity<MessageResponse> deletePost(@PathVariable("postId") String postId, Principal principal){
        postService.deletePostById(Long.parseLong(postId),principal);
        return new ResponseEntity<>(new MessageResponse("Post was deleted"), HttpStatus.OK);
    }
}
