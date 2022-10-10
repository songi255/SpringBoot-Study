package com.springboot.study.domain.posts;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest // 별다른 설정 없이 @SpringBootTest를 사용하면 H2 DB를 자동으로 실행해준다.
public class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository; // 아까 만든 interface. JPA에 의해 Posts에 매칭되는 CRUD가 자동으로 생성된다.

    @After // Junit에서 UnitTest 끝날 때마다 수행될 메서드 지정
    // 보통은 배포 전 전체테스트 수행 시, 테스트간 데이터 침범을 막기 위해 사용
    public void cleanup(){
        postsRepository.deleteAll();
    }

    @Test
    public void post_save_and_load() {
        //given
        String title = "테스트 게시글";
        String content = "테스트 본문";

        postsRepository.save(Posts.builder() // posts 테이블 (className으로 매핑된 테이블)에 insert/update 쿼리 실행
                .title(title)
                .content(content)
                .author("jojoldu@gmail.com")
                .build()); // id값이 있으면 update, 없으면 insert가 실행된다. 여기서는 auto_increment로 설정되있으니 없어도 문제없는듯..

        //when
        List<Posts> postsList = postsRepository.findAll(); // repository(Posts와 연결된)를 통해 posts 테이블에서 모든 데이터를 가져온다.

        Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);
    }

    // 이런식으로, save(), findAll() 등을 호출하면 자동으로 쿼리가 만들어져서 수행되었다. 그러면, 실제로 실행된 쿼리는 어디서 볼 수 있을까?
    // 물론 로그에 ON/OFF 할 수 있고, 이런 설정들을 Java Class로 구현할 수 있으나, Boot에서는 파일로 설정하도록 지원하고 권장한다.
    // 우선, resource 폴더에 application.properties를 만들자. 나머지는 거기에 적겠다.

    // jpa auditing test
    @Test
    public void register_base_time_entity(){
        //given
        LocalDateTime now = LocalDateTime.of(2022,10,10,0,0,0);
        postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());

        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts = postsList.get(0);

        System.out.println(">>>>>>>>> createDate=" + posts.getCreatedDate() + ", modifiedDate=" + posts.getModifiedDate());

        assertThat(posts.getCreatedDate()).isAfter(now);
        assertThat(posts.getModifiedDate()).isAfter(now);
        // JPA auditing 으로 날짜가 잘 들어간 것을 볼 수 있다.
    }
}

