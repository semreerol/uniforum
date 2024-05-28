package com.bunyaminkalkan.api.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    Long userId;
    String message;
    String accessToken;
    String refreshToken;
}
