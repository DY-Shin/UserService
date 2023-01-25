package com.ssafy.commonpjt.api.dto;

import lombok.Data;

@Data
public class UserDto {

    // 회원가입시 입력 정보
    private Long id;
    private String userId;
    private String userPassword;
    private String userPhone;
    private String userName;
    private String corporateRegistrationNumber;
    private String userAddress;
}
