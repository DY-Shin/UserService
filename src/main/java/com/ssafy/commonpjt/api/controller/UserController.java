package com.ssafy.commonpjt.api.controller;

import com.ssafy.commonpjt.api.dto.ResponseDto;
import com.ssafy.commonpjt.api.dto.UserDto;
import com.ssafy.commonpjt.api.dto.UserLoginRequestDto;
import com.ssafy.commonpjt.api.dto.UserLogoutRequestDto;
import com.ssafy.commonpjt.api.service.UserService;
import com.ssafy.commonpjt.common.jwt.JwtTokenProvider;
import com.ssafy.commonpjt.common.lib.Helper;
import com.ssafy.commonpjt.common.security.SecurityUtil;
import com.ssafy.commonpjt.db.entity.User;
import com.ssafy.commonpjt.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final ResponseDto response;

    // 회원가입
    // Postman 사용 시 @RequestBody 를 제거해야 form-data 로 확인 가능
    @PostMapping("")
    public ResponseEntity<?> join(@Validated @RequestBody UserDto userDto, Errors errors) throws Exception {
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return userService.join(userDto);
    }

    // 로그인
    @PostMapping("login")
    public ResponseEntity<?> login(@Validated @RequestBody UserLoginRequestDto login, Errors errors) {
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return userService.login(login);
    }


    // 로그아웃
    @PostMapping("logout")
    // USER 권한을 가지고있어야 실행 가능
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<?> logout(@Validated @RequestBody UserLogoutRequestDto logout, Errors errors) {
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return userService.logout(logout);
    }


    // 회원 탈퇴
    @DeleteMapping("delete")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<?> delete(@Validated @RequestBody UserLogoutRequestDto logout, Errors errors) throws Exception {
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return userService.delete(logout);
    }

    // 회원 정보 조회
    @GetMapping("me")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<User> getMyUserInfo() {
        return ResponseEntity.ok(userService.getMyUser().get());
    }
}