package com.springboot.study.web.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter // lombok. 선언된 모든 필드의 getter를 생성한다.
@RequiredArgsConstructor // lombok. 선언된 모든 final 필드를 포함시켜 생성자를 만들어준다.
// final이 없으면 생성자에 포함하지 않는다.
public class HelloResponseDto {

    private final String name;
    private final int amount;

}
