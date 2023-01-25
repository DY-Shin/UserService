package com.ssafy.commonpjt.api.dto;

import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class UserLoginRequestDto {
    // 로그인시 입력 정보
    private String userId;
    private String userPassword;


    // 로그인 시 입력받은 Id, Password 로 토큰 생성
    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(userId, userPassword);
    }
}
