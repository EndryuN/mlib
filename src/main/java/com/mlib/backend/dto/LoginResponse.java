package com.mlib.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor  // ✅ This enables new LoginResponse(token, username, email)
@NoArgsConstructor   // Keep this for deserialization
public class LoginResponse {
    private String token;
    private String username;
    private String email;
}