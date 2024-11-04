## Oauth2 소셜 로그인
- Feign을 사용해 각 provider API를 호출
### 전체 흐름:
1. **최초 소셜 로그인 (ex; 구글로 첫 로그인)**
   
   (1) 클라이언트가 구글 로그인 클릭 <br>
   (2) 구글 로그인 페이지로 리다이렉트<br>
   (3) 구글 인증 완료 -> 백엔드로 인증 코드 전달<br>
   (4) 새로운 User와 SocialUser(google) 생성<br>
   (5) 로그인 완료<br>
3. **두 번째 소셜 계정 연동 (ex; 카카오)**
   
   (1) 사용자가 소셜 계정 추가 연동 클릭 -> 카카오 계정 선택<br>
   (2) 임시 토큰 생성하여 Redis에 저장 -> ex; {key: "link:user123123", value: "userId:1", TTL:5}<br>
   (3) 카카오 로그인 페이지로 리다이렉트 (임시 토큰을 state 파라미터로 전달)<br>
   (4) 카카오 인증 완료 -> 백엔드로 인증 코드와 state(임시 토큰) 전달<br>
   (5) Redis에서 토큰으로 userId 확인<br>
   (6) 기존 User에 새로운 SocialUser(kakao) 연결<br>

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

### kakao
1. 카카오 로그인 페이지 -> 인증 코드 발급(kakaoAuthClient)
2. 액세스 토큰으로 사용자 정보 요청(kakaoApiClient)
3. 사용자 정보로 로컬 시스템 로그인(userService)
