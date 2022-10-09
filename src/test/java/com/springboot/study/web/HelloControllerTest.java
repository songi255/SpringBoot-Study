package com.springboot.study.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;

// 아래 static import는 멀까..? 여튼 자동완성은 잘 안되는듯하다.
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class) // test 진행 시 JUnit 내장 실행자 외에 다른 실행자 실행. 즉, SpringBootTest와 JUnit 사이의 연결자 역할
@WebMvcTest(controllers = HelloController.class) // 여러 SpringTestAnnotation 중 WemMVC에 집중할 수 있는 어노테이션
// 선언할 경우, @Controller, @ControllerAdvice 등을 사용 가능
// 단, @Service, @Component, @Repository 등은 사용 불가능
// 이 예제에서는 Controller만 사용하기 때문에 선언한다.
public class HelloControllerTest { // 일반적으로 TestClass는 대상 클래스 이름에 Test를 붙인다.

    @Autowired // MockMvc는 Spring이 관리하는 Bean인가봄. (애초에 프로젝트 의존성에 test가 있어서 그런듯?)
    private MockMvc mvc; // 웹 API 테스트시 사용
    // 스프링 MVC test 의 시작점임.
    // 이 MockMvc 클래스를 통해 HTTP GET/POST 등 API 테스트 가능

    @Test
    public void should_return_hello() throws Exception{ // 다 작성했다면, 이 메서드 옆의 화살표를 눌러서 실행해보자.
        String hello = "hello";

        mvc.perform(get("/hello")) // MockMvc를 통해 get 요청을 보냄. 체이닝 지원
                .andExpect(status().isOk()) // status가 200인지(isOk()) 검증
                .andExpect(content().string(hello)); // 응답본문 검증. hello를 리턴하는지 검증
    }

    // test를 실행하였고, success를 받았다. SpringBoot 로그가 찍힌다. Tomcat을 썻다는 log는 안나오네..
    // main을 실행해보면 localhost:8080/hello로 잘 접속되는 것을 볼 수 있다! tomcat을 쓰네~

    // 브라우저 검증보다, 이 testCode가 먼저다. 항상 testcode는 계속 작성하도록 하자

    // 이렇게 만들어놓은 testcode가 항상 우리의 코드를 지켜주기 때문에, 기존 파일을 수정할 때 문제가 생기는지 안생기는지 테스트코드만 돌려보면 된다.
    // 그래서 수정에 자유롭다~

    // 두번째, Dto 테스트~ JSON 응답을 test 해본다.
    @Test
    public void should_return_dto() throws Exception{
        String name = "hello";
        int amount = 1000;

        mvc.perform(
                get("/hello/dto")
                    .param("name", name) // API 테스트 시 사용될 요청 파라미터를 설정한다. 단 값은 String만 허용된다.
                    .param("amount", String.valueOf(amount))) // 그래서 숫자/날짜 등의 데이터도 등록할 때는 이런식으로 문자열로 변경해야 한다.
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name))) // restController로 설정했으니까.. json응답값을 필드별로 검증할 수 있는 메소드이다.
                .andExpect(jsonPath("$.amount", is(amount))); // $를 기준으로 필드명을 명시한다.
    }
}
