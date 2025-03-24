package com.bam.lemmecook.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetails {
    private int id;
    private String email;
    private String username;
    private String role;
}
