package com.instacopy.instacopy.web;

import com.instacopy.instacopy.dto.CommentDTO;
import com.instacopy.instacopy.entity.Comment;
import com.instacopy.instacopy.facade.CommentFacade;
import com.instacopy.instacopy.payload.response.MessageResponse;
import com.instacopy.instacopy.service.CommentService;
import com.instacopy.instacopy.validations.ResponseErrorValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    CommentFacade commentFacade;
    @Autowired
    ResponseErrorValidation responseErrorValidation;

    @PostMapping("/{postId}/create")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CommentDTO commentDTO,
                                                @PathVariable("postId") String postId,
                                                BindingResult bindingResult,
                                                Principal principal){

        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if(!ObjectUtils.isEmpty(errors)) return errors;

        Comment comment = commentService.saveComment(Long.parseLong(postId), commentDTO, principal);
        CommentDTO createdComment = commentFacade.commetTocommentDTO(comment);

        return new ResponseEntity<>(createdComment, HttpStatus.OK);
    }

    @GetMapping("{postId}/all")
    public ResponseEntity<List<CommentDTO>> getAllCommentPosts(@PathVariable("postId") String postId){
        List<CommentDTO> allCommentsForPost = commentService.getAllCommentsForPost(Long.parseLong(postId))
                .stream()
                .map(commentFacade::commetTocommentDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(allCommentsForPost,HttpStatus.OK);
    }

    @PostMapping("{commentId}/delete")
    public ResponseEntity<MessageResponse> deleteComment(@PathVariable("commentId") String commentId){
        commentService.deleteComment(Long.parseLong(commentId));
        return new ResponseEntity<>(new MessageResponse("Comment was deleted"),HttpStatus.OK);
    }
}
