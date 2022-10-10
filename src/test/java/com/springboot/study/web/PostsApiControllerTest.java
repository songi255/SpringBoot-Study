package com.springboot.study.web;


import com.springboot.study.domain.posts.Posts;
import com.springboot.study.domain.posts.PostsRepository;
import com.springboot.study.web.dto.PostsSaveRequestDto;
import com.springboot.study.web.dto.PostsUpdateRequestDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @WebMvcTest 를 사용하지 않았다. 왜냐하면 WebMvc는 JPA 기능이 동작하지 않기 때문이다. (Controller, ControllerAdvice 등 외부연동과 관련된 부분만 활성화된다.)
// 고로 지금처럼 JPA까지 한번에 test할 때는 @SpringBootTest와 TestRestTemplate를 사용하면 된다.
public class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @After
    public void tearDown() throws Exception{
        postsRepository.deleteAll();
    }

    @Test
    public void upload_post() throws Exception{
        //given
        String title = "title";
        String content = "content";
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author("author")
                .build();
        String url = "http://localhost:" + port + "/api/v1/posts";

        //when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }

    // 두번째 테스트
    @Test
    public void modify_posts() throws Exception {
        //given
        Posts savedPosts = postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());

        Long updateId = savedPosts.getId();
        String expectedTitle = "title2";
        String expectedContent = "content2";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;

        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        //when
        ResponseEntity<Long> responseEntity = restTemplate
                .exchange(url, HttpMethod.PUT, requestEntity, Long.class);
        //Put 요청에 의해 Posts의 update가 발생하고, 실제 DB에 update 쿼리가 쳐진 것을 확인할 수 있다.

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
    }

    // 이제 조회기능은 실제로 톰캣을 실행해서 확인해보겠다.
    // 앞서 말한 대로 local 환경에서는 DB로 H2를 사용한다. 메모리에서 실행하기 때문에, 직접 접근하기 위해서는 웹 콘솔을 이용해야 한다.
    // 웹 콘솔 활성화를 위해 application.properties에 옵션을 추가해주자.
    // main을 실행하고, 브라우저로 접근해보자. localhost:8080/h2-console 이다.
    // jdbc url을 jdbc:h2:mem:testdb로 수정해준다. connect 해보자. 현재 프로젝트의 H2의 관리페이지로 간다.
    // POSTS 테이블이 정상적으로 노출되는 지 확인한다.
    // Select * from posts; 간단한 쿼리를 실행해보자.
    // insert into posts (author, content, title) values ('author', 'content', 'title'); insert 해보자.
    // localhost:8080/api/v1/posts/1 로 접속하여 api 조회를 테스트해보자. 잘 작동하는 것을 확인할 수 있다.

    // 이제 기본적인 등록/수정/조회 기능을 모두 만들고 테스트했다. 특히 등록/수정은 테스트코드로 보호해주고 있으니 이후 변경도 안전하게 할 수 있다.
}
