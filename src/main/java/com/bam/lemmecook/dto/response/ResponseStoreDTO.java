package com.bam.lemmecook.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseStoreDTO {
    private int id;
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private double distance;
    private int ingredientId;
    private String ingredientName;
    private double price;
    private int stock;
}
