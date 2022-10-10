package com.springboot.study.web.dto;

import com.springboot.study.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {
    private String title;
    private String content;
    private String author;

    @Builder
    public PostsSaveRequestDto(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public Posts toEntity(){
        return Posts.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();
    }

    /* 애초에 Dto가 있는데, 거의 비슷한 형태로 Dto를 다시 만든 모습이다. 왜? 절대로 Entity 클래스를 Request/Response 클래스로 사용해서는 안되기 때문이다.
        - Entity 클래스는 DB와 맞닿은 핵심클래스이다. Entity를 기준으로 table이 생성되고, Schema가 변경된다.
        - 화면변경은 아주 사소한 기능변경인데, 이를 위해서 Entity까지 변경하는 것은 너무 큰 변경이다.
        - 수많은 Service class나 비즈니스 로직들이 Entity 클래스를 기준으로 동작하니, 변경은 여러클래스에 영향을 끼친다.
        - Request/Response 용 Dto는 View를 위한 Class 이기 때문에 정말 자주 변경이 필요하다.
        - 즉, View Layer와 DB Layer의 역할분리를 철저히 하는 게 좋다. 실제로 Controller에서 여러 테이블을 join한 결과를 응답해야 할 경우가 빈번해서
        - Entity 만으로 표현하기 어려운 경우가 많다.
        - 한번 더 말한다. Entity 클래스와 Controller에서 쓸 Dto는 꼭 분리해서 사용해야 한다!!!!


     */
}
