package com.springboot.study.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 이 클래스를 상속할 경우 여기있는 필드들 (createdDate, modifiedDate) 도 칼럼으로 인식하도록 한다.
@EntityListeners(AuditingEntityListener.class) // Auditing 기능을 포함시킨다.
public class BaseTimeEntity {
    /* 보통 Entity는 해당 데이터의 생성/수정시간을 포함한다. 차후 유지보수에 매우 중요한 정보이기 때문이다.
    AOP처럼, JPA Auditing을 사용해서, JPA에서 insert, update 하기 전 자동으로 날짜 데이터를 등록/수정하도록 해보자.

    참고로, Java8부터는 LocalDate, LocalDateTime을 사용한다.
        - 기존 존재하던 Date, Calendar의 문제점을 제대로 고친 타입이다.
            - 둘은 불변객체가 아니라서, 멀티스레드에서 문제가 생길 수 있었다.
            - Calendar의 Month는 OCTOBER가 10이 아니고 9여서 많은 혼란이 있었다.

    지금 만드는 이 BaseTimeEntity 클래스가 모든 Entity의 상위클래스가 되어 생성/수정시간을 자동으로 관리할 것이다.
    이걸 기존 Posts가 상속하도록 수정할 것이다.

    또, JPA Auditing 어노테이션들을 모두 활성화할 수 있도록 Application 클래스에 활성화 어노테이션 하나를 추가한다.
    */

    @CreatedDate // 이 Entity가 생성되어 저장될 때 시간이 자동저장된다.
    private LocalDateTime createdDate;

    @LastModifiedDate // 조회한 Entity 값이 변경될 때 시간을 자동저장한다.
    private LocalDateTime modifiedDate;

}
