package com.instacopy.instacopy.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CommentDTO {

    private Long id;
    @NotEmpty
    private String username;
    private String message;

}
