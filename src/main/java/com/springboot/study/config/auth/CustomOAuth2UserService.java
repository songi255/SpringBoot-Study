package com.springboot.study.config.auth;

import com.springboot.study.config.auth.dto.OAuthAttributes;
import com.springboot.study.config.auth.dto.SessionUser;
import com.springboot.study.domain.user.User;
import com.springboot.study.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // 현재 로그인 진행중인 서비스 구분하는 코드
        // 지금은 구글만 사용하는 불필요한 값이지만, 이후 네이버 등등 연동 시..
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        // OAuth2 로그인 시 Key가 되는 필드값. Primary Key와 같은 의미이다.
        // 구글의 경우 기본적으로 코드 지원하지만("sub"), 네이버 카카오 등은 기본지원하지 않는다.
        // 이후 네이버로그인, 구글 로그인 동시지원 시에 사용됨

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        // OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담을 클래스. 이후 네이버 등에도 이 클래스를 사용한다.
        
        User user = saveOrUpdate(attributes);

        httpSession.setAttribute("user", new SessionUser(user)); // 세션에 사용자 정보를 저장하기 위한 Dto 클래스
        // 왜 User 클래스를 쓰지 않고 새로 만드는지?
        // Session에 저장하기 위해선 직렬화가능해야 한다. 그럼 User에 직렬화 넣으면 어떻게 되냐?
        // Entity 클래스는 언제 다른 Entity와 관계가 형성될 지 모른다.
        // 예를 들어, @OneToMany, @ManyToMany 등.. 자식 Entity를 가지고 있다면 직렬화대상에 자식까지 포함되니 성능이슈, 부수효과가 발생할 확률이 높다.
        // 그래서 직렬화 기능을 가진 세션 Dto를 하나 추가로 만드는 것이 이후 운영 및 유지보수에 많은 도움이 된다.


        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    // 구글 사용자정보가 update될 때를 대비해서 update 기능도 구현했다. name이나 picture이 변경되면 User 엔티티에도 반영된다.
    private User saveOrUpdate(OAuthAttributes attributes){
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}
