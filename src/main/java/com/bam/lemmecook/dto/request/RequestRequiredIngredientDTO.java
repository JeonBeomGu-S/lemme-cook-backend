package com.bam.lemmecook.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestRequiredIngredientDTO {
    private int id;
    private String quantity;
}
