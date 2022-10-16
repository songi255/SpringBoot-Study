package com.springboot.study.config.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) // Parameter 에 적용하겠다는 뜻. 이 어노테이션이 생성될 수 있는 위치를 의미한다.
@Retention(RetentionPolicy.RUNTIME) // 이게 아마 RUNTIME에 사용하겠다는 정책설정이었지...
public @interface LoginUser {

}
