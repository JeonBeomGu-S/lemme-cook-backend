package com.bam.lemmecook.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestSignupDTO {
    private String username;
    private String email;
    private String password;
    private String role;
}
