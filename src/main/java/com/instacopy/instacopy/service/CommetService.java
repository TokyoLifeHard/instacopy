package com.instacopy.instacopy.service;

import com.instacopy.instacopy.dto.CommentDTO;
import com.instacopy.instacopy.entity.Comment;
import com.instacopy.instacopy.entity.Post;
import com.instacopy.instacopy.entity.User;
import com.instacopy.instacopy.exeptions.PostNotFoundException;
import com.instacopy.instacopy.repository.CommentRepository;
import com.instacopy.instacopy.repository.PostRepository;
import com.instacopy.instacopy.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class CommetService {

    public static final Logger LOG = LoggerFactory.getLogger(CommetService.class);

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommetService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Comment saveComment(Long postId,CommentDTO commentDTO, Principal principal){
        User user = getUserByPrincipal(principal);
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new PostNotFoundException("Post can not be found for username: "+user.getUsername()));
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUserId(user.getId());
        comment.setUsername(user.getUsername());
        comment.setMesasge(commentDTO.getMessage());

        LOG.info("Saving Commet for Post {}",post.getId());
        return commentRepository.save(comment);
    }

    public List<Comment> getAllCommentsForPost(Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new PostNotFoundException("Post can not be found"));
        return commentRepository.findAllByPost(post);
    }

    public void deleteComment(Long commentId){
        Optional<Comment> comment = commentRepository.findById(commentId);
        comment.ifPresent(commentRepository::delete);
    }

    private User getUserByPrincipal(Principal principal){
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("User with username "+username+" not found"));
    }
}
