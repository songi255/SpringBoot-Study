package com.springboot.study.web.dto;

import com.springboot.study.domain.posts.Posts;
import lombok.Getter;

@Getter
public class PostsResponseDto {
    private Long id;
    private String title;
    private String content;
    private String author;

    public PostsResponseDto(Posts entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
    }
    // Entity의 필드 중 일부만 사용하므로 굳이 모든 필드를 가진 생성자가 필요하진 않다. 그래서 Entity를 받아 처리한다.
}
