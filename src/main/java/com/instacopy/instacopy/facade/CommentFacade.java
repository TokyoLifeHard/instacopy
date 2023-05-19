package com.instacopy.instacopy.facade;

import com.instacopy.instacopy.dto.CommentDTO;
import com.instacopy.instacopy.entity.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentFacade {

    public CommentDTO commetTocommentDTO(Comment comment){
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setUsername(comment.getUsername());
        commentDTO.setMessage(comment.getMesasge());

        return commentDTO;
    }
}
