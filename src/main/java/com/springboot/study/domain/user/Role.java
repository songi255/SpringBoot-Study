package com.springboot.study.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    // Spring Security 에서는 권한코드 앞에 항상 ROLE_ 이 있어야 한다.
    GUEST("ROLE_GUEST", "손님"),
    USER("ROLE_USER", "일반사용자");

    private final String key;
    private final String title;
    // enum을 이렇게도 쓸 수 있었지..
}
