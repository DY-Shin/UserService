# 회원관리 서비스

## 구현한 기능

- jwt 토큰을 이용한 회원가입, 로그인

- Redis로 jwt를 만료시켜 로그아웃

- 회원가입 시 비밀번호 암호화 저장

- 회원 탈퇴 시 DB에서 삭제

- 내 정보 조회, 수정 및 유저 검색

## 추가하고 싶은 것

- 이름이나 핸드폰 번호 중복 검사

- 중복 로그인 안되게

## 했던 뻘짓

- 시큐리티 설정에 USER라고 Role를 넣어뒀으면서 Role을 안쓰려고 했다... 안쓰면 로그아웃이 안됨

- 세션으로 했으면 간단한거를 갖다가 왜 jwt로해서 로그아웃이 이렇게 어려운지 (토큰 삭제만 하면 되는 세션 << 토큰 만료 후 블랙리스트 등록해야하는 jwt)

- 비밀번호 암호화 복호화가지고 씨름했다.. 이건 대체 왜 안되는거람 암호화할때 키도 지맘대로라서 두번 암호화해서 비교해도 BadCredential에러 (비밀번호 오류) - CustomUserService에서 암호화를 또 하면 안됨

- Username 쓰면안됨 정보검색이 안댐 - postman header에 토큰을 안넣고 검사함

```java
    @Modifying
    @Query(value = "set FOREIGN_KEY_CHECKS = 0;", nativeQuery = true)
    @Transactional
    public void foreignKeyDelete();

   @Modifying
   @Query("delete from User where userId = :userId")
   @Transactional
   public void delete(@Param("userId") String userId);

    @Modifying
    @Query(value = "set FOREIGN_KEY_CHECKS = 1;", nativeQuery = true)
    @Transactional
    public void foreignKeyCheck();
```

- deleteById() 대신 delete() 쓰니까 쿼리문 싹 지워버림
