package com.springboot.study.service.posts;

import com.springboot.study.domain.posts.Posts;
import com.springboot.study.domain.posts.PostsRepository;
import com.springboot.study.web.dto.PostsListResponseDto;
import com.springboot.study.web.dto.PostsResponseDto;
import com.springboot.study.web.dto.PostsSaveRequestDto;
import com.springboot.study.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;
    // Bean 주입 @AutoWired가 왜 없을까? 어색하다.
    // Spring의 Bean 주입 방법은 다음과 같다. @AutoWired, setter, 생성자
    // 이 중 가장 권장하는 방식이 생성자주입 방식이다. (@AutoWired는 권장하지 않는다.)
    // Lombok으로 생성자를 만들기 때문에 변경이 일어나도 생성자코드는 전혀 손대지 않아도 된다.

    @Transactional
    public Long save(PostsSaveRequestDto requestDto){
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto){
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }
    /*update에서 query를 날리는 부분이 없다. 심지어, Posts에서도 update는 단순히 setter이다. 이게 가능한 이유는 JPA의 영속성 컨텍스트 때문이다.
      - 영속성컨텍스트란, Entity를 영구저장하는 환경인데, 일종의 논리적 개념이라고 보면 된다.
      - JPA의 핵심내용은 Entity가 영속성 컨텐스트에 포함되어있냐 아니냐로 갈린다??
      - JPA의 EntityManager가 활성화된 상태로 Transaction 안에서 DB에서 데이터를 가져오면 이 데이터는 영속성컨텐스트가 유지된 상태이다.
        - EntityManager가 활성화 된 것은 Spring Data Jpa를 쓴다면 기본값이다.
      - 이 상태에서 해당 데이터의 값을 변경하면 Transaction이 끝나는 시점에 해당 table에 변경분을 반영한다.
      - 즉, Entity에 setting만 하면 Update 쿼리를 날릴 필요가 없다. 이를 dirty checking 이라고 한다.
        - 처음 Entity로 조회하면, 스냅샷을 찍어놓고, 트랜잭션이 끝날 때 비교한다.
        - @Transactional 과 함께하는 것이 이것이다.
        - 만약 전체 Column을 업데이트하는게 부담스럽다면 @DynamicUpdate를 class에 붙이면 된다.
            - 다만 이게 부담스러울 정도로 Column이 20~30개씩 되고 한다면, 애초에 정규화가 잘못되어있을 확률이 크다.



     */

    public PostsResponseDto findById(Long id){
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        return new PostsResponseDto(entity);
    }

    @Transactional(readOnly = true) // 트랜잭션 범위는 유지하되, 조회기능만 남겨두어 조회속도를 개선할 수 있다.
    // 그래서, 등록/수정/삭제 기능이 전혀 없는 서비스메소드에서 사용하는 것을 추천한다.
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        postsRepository.delete(posts);
    }
}
