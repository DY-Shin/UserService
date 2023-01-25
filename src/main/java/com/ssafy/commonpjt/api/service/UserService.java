package com.ssafy.commonpjt.api.service;

import com.ssafy.commonpjt.api.dto.*;
import com.ssafy.commonpjt.common.enums.Authority;
import com.ssafy.commonpjt.common.jwt.JwtTokenProvider;
import com.ssafy.commonpjt.common.security.SecurityUtil;
import com.ssafy.commonpjt.db.entity.User;
import com.ssafy.commonpjt.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ResponseDto response;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;

    // 회원가입 서비스
    public ResponseEntity<?> join(UserDto userDto) throws Exception {
        // 회원 중복 확인
        if (userRepository.existsByUserId(userDto.getUserId())){
            return response.fail("이미 회원가입된 아이디입니다.", HttpStatus.BAD_REQUEST);
        }
        // DB에 저장
        User user = User.builder()
                .userId(userDto.getUserId())
                .userPassword(passwordEncoder.encode(userDto.getUserPassword()))
                .userPhone(userDto.getUserPhone())
                .userName(userDto.getUserName())
                .userCorporateRegistrationNumber(userDto.getCorporateRegistrationNumber())
                .userAddress(userDto.getUserAddress())
                .roles(Collections.singletonList(Authority.USER.name()))
                .build();
        userRepository.save(user);
        return response.success("회원가입에 성공했습니다.");
    }

    // 로그인 서비스
    public ResponseEntity<?> login(UserLoginRequestDto login) {
        // 회원 정보 조회
        if (userRepository.findByUserId(login.getUserId()).orElse(null) == null) {
            return response.fail("해당하는 유저가 존재하지 않습니다", HttpStatus.BAD_REQUEST);
        }
        // 로그인시 토큰 생성
        UsernamePasswordAuthenticationToken authenticationToken = login.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenDto tokenDto = jwtTokenProvider.generateToken(authentication);
        // 토큰 정보 설정
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), tokenDto.getRefreshToken(),
                        tokenDto.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);
        return response.success(tokenDto, "로그인에 성공했습니다.", HttpStatus.OK);
    }

    // 로그아웃 서비스
    public ResponseEntity<?> logout(UserLogoutRequestDto logout) {
        // 토큰 유효성 검사
        if (!jwtTokenProvider.validateToken(logout.getAccessToken())) {
            return response.fail("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        }
        // 토큰이 유효하다면 access 토큰을 받아옴
        Authentication authentication = jwtTokenProvider.getAuthentication(logout.getAccessToken());
        if (redisTemplate.opsForValue().get("RT:" + authentication.getName()) != null) {
            redisTemplate.delete("RT:" + authentication.getName());
        }
        // 토큰을 즉시 만료시키고 블랙리스트에 등록
        Long expiration = jwtTokenProvider.getExpiration(logout.getAccessToken());
        redisTemplate.opsForValue()
                .set(logout.getAccessToken(), "logout", expiration, TimeUnit.MILLISECONDS);
        return response.success("로그아웃 되었습니다.");
    }

    // 유저 정보 검색
    @Transactional(readOnly = true)
    public Optional<User> getUser(String userId) {
        return userRepository.findByUserId(userId);
    }

    // 내 정보 검색
    @Transactional(readOnly = true)
    public Optional<User> getMyUser() {
        return SecurityUtil.getCurrentUsername().flatMap(userRepository::findByUserId);
    }

    // 회원 탈퇴
    @Transactional
    public ResponseEntity<?> delete(UserLogoutRequestDto logout) throws Exception {
        if (!jwtTokenProvider.validateToken(logout.getAccessToken())) {
            return response.fail("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        }
        Authentication authentication = jwtTokenProvider.getAuthentication(logout.getAccessToken());
        String userId = authentication.getName();
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new Exception("유저 없음"));
        if (!user.matchPassword(passwordEncoder, logout.getCheckPassword())){
            return response.fail("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        userRepository.foreignKeyDelete();
        userRepository.delete(user);
        userRepository.foreignKeyCheck();
        logout(logout);
        return response.success("회원 탈퇴되었습니다.");
    }
}

