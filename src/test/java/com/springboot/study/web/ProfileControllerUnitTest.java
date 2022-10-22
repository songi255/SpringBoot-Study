package com.springboot.study.web;

import org.junit.Test;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

// 특별히 Spring 환경이 필요하지는 않아서 @SpringBootTest는 뺐다. 대상인 Controller와 Enviroment 둘 다 Java Class라서..
public class ProfileControllerUnitTest {
    @Test
    public void real_profile_searched(){
        // given
        String expectedProfile = "real";
        // Enviroment는 인터페이스인데, Spring에서 Mock을 제공한다.
        MockEnvironment env = new MockEnvironment();
        // 이걸 보면 생성자DI가 얼마나 유용한 지 알 수 있다. 만약 Enviroment를 @AutoWired로 DI 받았다면 이런 테스트코드는 작성하지 못하고,
        // 항상 SpringTest만 사용해야했을 것이다.
        env.addActiveProfile(expectedProfile);
        env.addActiveProfile("oauth");
        env.addActiveProfile("real-db");

        ProfileController controller = new ProfileController(env);

        // when
        String profile = controller.profile();
        // profile()이 인증없이도 호출될 수 있게 SecurityConfig에 제외코드를 추가해주자.

        // then
        assertThat(profile).isEqualTo(expectedProfile);
    }

    @Test
    public void search_first_if_not_exist_real_profile(){
        // given
        String expectedProfile = "oauth";
        MockEnvironment env = new MockEnvironment();
        env.addActiveProfile(expectedProfile);
        env.addActiveProfile("real-db");

        ProfileController controller = new ProfileController(env);

        // when
        String profile = controller.profile();

        // then
        assertThat(profile).isEqualTo(expectedProfile);
    }

    @Test
    public void search_default_if_not_exist_active_profile(){
        // given
        String expectedProfile = "default";
        MockEnvironment env = new MockEnvironment();

        ProfileController controller = new ProfileController(env);

        // when
        String profile = controller.profile();

        // then
        assertThat(profile).isEqualTo(expectedProfile);
    }
}
