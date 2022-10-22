package com.springboot.study.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

// SecurityConfig 에 인증 예외설정이 잘 되었는지 테스트
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProfileControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void call_profile_without_authorization(){
        String expected = "default";

        ResponseEntity<String> response = restTemplate.getForEntity("/profile", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expected);
    }

    // 여기까지 모든 테스트가 성공했다면 깃허브로 푸시해서 배포. 브라우저에서 /profile로 접속해서 잘 나오는지 확인한다.
    // 여기까지 되었으면 잘 구성된것이니 다음으로 넘어간다.
}
