package com.bam.lemmecook.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseRecipeDTO {
    private int id;
    private int userId;
    private String name;
    private String description;
    private String instructions;
    private Date createdAt;
    private Date updatedAt;
}
