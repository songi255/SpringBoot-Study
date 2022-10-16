package com.springboot.study.config.auth;

import com.springboot.study.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity // Spring security 설정들 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable() // 위 전부 h2-console 화면 사용하기 위해 해당 옵션들 disable
                .and()
                    .authorizeRequests() // URL별 권한관리 설정옵션의 시작. 이게 선언되어야 andMatchers 옵션을 사용할 수 있음
                    // 권한관리대상 지정. URL, HTTP Method 별로 관리할 수 있음
                    .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll() // 전부 허용
                    .antMatchers("/api/v1/**").hasRole(Role.USER.name()) // USER 권한 가진 사람만 가능
                    .anyRequest().authenticated() // anyRequest = 그 외 나머지 URL. 나머지는 전부 인증된 사용자들(즉, 로그인한 사용자들)에게만 허용
                .and()
                    .logout() // 로그아웃 기능에 대한 여러 설정의 진입점
                        .logoutSuccessUrl("/") // 로그아웃 성공 시 /로 이동
                .and()
                    .oauth2Login() // Oauth2 로그인 기능에 대한 설정 진입점
                        .userInfoEndpoint() // 로그인 성공 이후 사용자정보를 가져올 때의 설정들을 담당
                            .userService(customOAuth2UserService); // 소셜로그인 성공 시 후속조치를 할 UserService 인터페이스의 구현체 등록
    }
}
