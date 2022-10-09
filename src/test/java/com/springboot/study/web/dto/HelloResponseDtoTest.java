package com.springboot.study.web.dto;

import static org.assertj.core.api.Assertions.assertThat;
/* 일부러 Junit의 기본 assertThat이 아닌 assertj의 assertThat을 사용했다.
    assertj 역시 JUnit에서 자동으로 라이브러리 등록해준다. JUnit에 비해 assertj의 장점은 다음과 같다.
        -CoreMatchers와 달리, 추가적인 라이브러리가 필요하지 않다.
            - JUnit의 경우 is() 같이 CoreMatchers 라이브러리가 필요하다.
        - 자동완성이 좀 더 확실하게 지원된다.
            - IDE에서는 CoreMatchers와 같은 Matcher 라이브러리 자동완성 지원이 약하다.
            - 고로 외울 필요가 없다. Matchers는 쓸 메서드를 외워야된다.

*/
import org.junit.Test;

public class HelloResponseDtoTest {

    @Test
    public void lombok_test() {
        // given
        String name = "test";
        int amount = 1000;

        //when
        HelloResponseDto dto = new HelloResponseDto(name, amount); // lombok으로 자동 생성된 생성자를 이미 확인할 수 있다.

        //then
        assertThat(dto.getName()).isEqualTo(name); // assertj 라는 테스트검증라이브러리의 검증메소드를 사용했다. 검증하고픈 대상을 메소드 인자로 받는다. 체이닝된다.
        assertThat(dto.getAmount()).isEqualTo(amount);

    }
}
