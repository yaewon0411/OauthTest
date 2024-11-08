## Oauth2 소셜 로그인
- Feign을 사용해 각 provider API를 호출
### 전체 흐름:
1. **최초 소셜 로그인 (ex; 구글로 첫 로그인)**
   
   (1) 클라이언트가 구글 로그인 클릭 <br>
   (2) 구글 로그인 페이지로 리다이렉트<br>
   (3) 구글 인증 완료 -> 백엔드로 인증 코드 전달<br>
   (4) 새로운 User와 SocialUser(google) 생성<br>
   (5) 로그인 완료<br>
2. **두 번째 소셜 계정 연동 (ex; 카카오)**
   
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



## 코드 리뷰
- state 의 역할 : 로그인 페이지로 리다이렉트 해준 서버랑 콜백 받은 서버가 똑같아?
  - 따라서 현재 임시 토큰을 사용한 연동 방식 외에 다른 방안을 생각해볼 필요 있음
- Converter 사용 제안
```java
public void socialLogin(@PathVariable(value = "provider") String provider, // kakao, naver, google
                            @RequestParam(required = false, value = "linkageToken") String linkageToken,
                            HttpServletResponse response) throws IOException {
        String authorizationUrl = oauth2Service.getAuthorizationUrl(provider, linkageToken);
        response.sendRedirect(authorizationUrl);
    }
```
  - 여기서 // Converter<T, S> -> class OAuth2ProviderConverter implements Converter<String, Provider> { ... } 와 같은 식으로 안전하게 타입 처리 권장

- 인증 url 생성하는 부분
```java
        // 이 규칙은 사실 제 생각에는 Provider 가 자율적으로 결정할 수 있으면 좋겠다!
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(oAuth2Provider.getAuthorizationUri())
                .queryParam("client_id", oAuth2Provider.getClientId())
                .queryParam("redirect_uri", oAuth2Provider.getRedirectUri())
                .queryParam("response_type", RESPONSE_TYPE)
                .queryParam("scope", String.join(" ", oAuth2Provider.getScopes()));
```

- 일반 로그인 처리와 계정 추가 연동하는 부분
```java
    //일반 로그인 처리
    //여기서도 안전하게 String이 아니라 Provider로 받는 것을 권장
    public LoginRespDto processOAuth2Callback(String provider, String authorizationCode){
        OAuth2Client<? extends TokenRespDto> client = oAuth2ClientFactory.getClient(provider);
        try{
            // 적당한 추상화수준!

            // 비즈니스 로직에 대한 설명!
            // 1. authorizationCode 로 accessToken 가져와
            String accessToken = client.getAccessToken(authorizationCode);
            // 2. accessToken 으로 사용자 정보 가져와
            OAuth2UserInfo userInfo = client.getUserInfo(accessToken);
            // 3. 이 사용자 정보로 로그인해
            // 3-1. 처음 로그인한 사용자면 회원가입처리해
            // 3-2. 토큰 발급해서 반환해
            return userService.login(userInfo, Provider.fromString(provider));
        } catch (FeignException e){
            log.error("OAuth2 프로세스 실패 - provider: {}, error: {}", provider, e.getMessage());
            throw new IllegalStateException("OAuth2 콜백 실패: {}"+e.getMessage(), e);
        }
    }
```
  - 그리고 일반 로그인과 계정 추가 연동 코드가 동일한 부분이 많으니 이 부분도 고민할 것을 권장

- userService의 login 메서드 -> optional을 쓰고 있으므로 좀 더 functional스럽게 사용하는 것을 권장
```java
    public LoginRespDto login(OAuth2UserInfo userInfo, Provider provider) {
    return socialUserRepository.findByProviderAndProviderId(provider, userInfo.getProviderId())
            .map(this::createLoginResp)
            .orElseGet(() -> {
                User user = User.builder()
                        .primaryEmail(userInfo.getEmail())
                        .primaryName(userInfo.getName())
                        .build();
                userRepository.save(user);
                SocialUser socialUser = SocialUser.builder()
                        .user(user)
                        .provider(provider)
                        .providerId(userInfo.getProviderId())
                        .email(userInfo.getEmail())
                        .name(userInfo.getName())
                        .build();
                socialUserRepository.save(socialUser);

                return createLoginResp(socialUser);
            });
}
```
- OAuth2ClientFactory 에서 OAuth2Client를 불러오고 있는 방식
  - OAuth2Client를 빈으로 등록해서 Factory에서 아래와 같은 방식으로 사용하면 좀 더 추상화 가능
  ```java
    // Key : Bean 이름
    // Value : Bean
    private final Map<String, OAuth2Client> clientMap;
  ```
  - getClient()도 Provider를 받도록 수정 권장
  ```java
    public OAuth2Client<? extends TokenRespDto> getClient(Provider provider){
        // Sealed Class
        return switch (provider) {
            case KAKAO -> getKakaoClient();
            case GOOGLE -> getGoogleClient();
            default -> throw new IllegalStateException("지원하지 않는 OAuth2 provider 입니다: " + provider);
        };
  }
   ```
  - 매번 각 provider 별 객체를 생성하고 있다 -> 빈으로 관리 권장
  ```java
     // GoogleOAuth2Client 가 Bean 이 아니다 -> 매번 만드는 것 같다!
    public OAuth2Client<GoogleTokenRespDto> getGoogleClient() {
        return new GoogleOAuth2Client(
                googleAuthClient,
                googleApiClient,
                oAuth2Properties.getProvider("google")
        );
    }
   ```

