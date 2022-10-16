package com.springboot.study.config.auth;

import com.springboot.study.config.auth.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final HttpSession httpSession;

    @Override
    public boolean supportsParameter(MethodParameter parameter) { // Controller 메서드의 특정 파라미터를 지원하는지 판단
        boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null; // 1. 파라미터에 @LoginUser 가 붙어있는가?
        boolean isUserClass = SessionUser.class.equals(parameter.getParameterType()); // 2. 파라미터 클래스 타입이 SessionUser 인가?

        return isLoginUserAnnotation && isUserClass;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // 파라미터에 전달할 객체를 생성한다.
        // 여기서는 Session에서 객체를 가져온다.
        return httpSession.getAttribute("user");
    }

    // 이제 이 클래스가 Spring이 인식할 수 있도록 WebMvcConfigurer에 추가하겠다. config - WebConfig 클래스에 설정을 추가하겠다.
}
