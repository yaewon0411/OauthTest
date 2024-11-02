## Oauth2 소셜 로그인
- Feign을 사용해 각 provider API를 호출
- 인증 흐름:
  1. 클라이언트가 소셜 로그인 버튼 클릭
  2. 해당 probider의 로그인 페이지로 리다이렉트
  3. 사용자 동의 후 인증 코드 발급
  4. 인증 코드로 액세스 토큰 요청
  5. 액세스 토큰으로 사용자 정보 조회
  6. 조회된 정보로 내부 시스템 로그인/회원가입 처리

### 메인 컴포넌트
1. **``OAuth2Controller``**
    - 소셜 로그인 시작점과 인증 코드를 받아 처리하는 콜백 엔드포인트 관리
    - Pathvariable 'provider'를 통해 각 OAuth2 provider에 연결한 로그인 진행
2. **``OAuth2Service``**
    - provider별 인증 URL 생성
    - 인증 코드로 액세스 토큰 획득한 후 사용자 정보 조회
3. **``OAuth2Client(interface)``**
    - 각 provider는 해당 인터페이스의 인증 토큰과 사용자 정보 가져오는 공통 메서드를 필수 구현
4. **``FeignClient``**
    - XXXAuthClient: 액세스 토큰 요청용 클라이언트
    - XXXApiClient: 사용자 정보 조회용 클라이언트


## 연결한 Provider
### google
1. 구글 로그인 페이지 -> 인증 코드 발급(googleAuthClient)
2. 액세스 토큰으로 사용자 정보 요청(googleApiClient)
3. 사용자 정보로 로컬 시스템 로그인(userService)