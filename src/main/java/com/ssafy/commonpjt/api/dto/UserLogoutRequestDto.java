package com.ssafy.commonpjt.api.dto;

import lombok.Data;

@Data
public class UserLogoutRequestDto {
    // 로그아웃시 필요한 토큰
    private String accessToken;
    private String refreshToken;
    private String checkPassword;
}
