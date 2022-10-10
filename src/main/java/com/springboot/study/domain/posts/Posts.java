package com.springboot.study.domain.posts;

import com.springboot.study.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// 필자는 어노테이션 순서를 주요 어노테이션을 Class에 가깝게 둔다고 한다.
@Getter
@NoArgsConstructor // 이건 NoArgs 니까, 아무 인자 없는 기본생성자를 추가해준다는 말.
@Entity // JPA의 어노테이션. 위 두 어노테이션은 Lombok이고, 코드가 단순해지지만 필수는 아니다. 그래서 주요어노테이션인 Entity를 가깝게 두었다.
// 이렇게 하면 이후 Kotlin으로의 전환 등 롬복이 더이상 필요없을 경우 쉽게 삭제할 수 있다.
// @Entity : table과 링크될 클래스임을 나타낸다. 기본값으로 Class의 camelCase 이름을 snakeCase로 바꾼 이름으로 table이름을 매칭한다.
public class Posts extends BaseTimeEntity { // 이 Posts 클래스는 실제 DB의 table과 매칭될 클래스이다. 보통 Entity Class 라고 한다.
    //JPA로 작업할 경우, 실제 쿼리를 날리기보다 이 Entity 클래스의 수정을 통해 작업할 수 있다.

    @Id // 해당 table의 PK필드를 나타낸다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PK의 생성규칙을 나타냄.
    // SpringBoot 2.0에서는 GenerationType.IDENTITY 옵션을 추가해야만 auto_increment가 된다.
    // 웬만하면 Entity의 PK는 Long타입(MySQL기준 bigint) 의 Auto_increment를 추천한다고 한다.
    //  - 주민번호같이 비즈니스상 유니크하거나, 복합키로 PK를 잡으면 난감한 일이 종종 발생한다
    //      - FK 맺을 때 마다 다른테이블에서 복합키를 모두 가지고 있거나, 중간테이블을 하나 더 둬야 하는 상황이 발생
    //      - 인덱스에 좋지 않은 영향 끼침
    //      - 유니크한 조건이 변경될 경우 PK 전체를 수정해야 하는 일 발생
    //      - 주민번호, 복합키 등은 유니크 키로 별도추가하는 것을 추천한다.
    private Long id;

    @Column(length = 500, nullable = false) // column을 나타내며, 굳이 선언하지 않아도, Entity Class의 필드는 모두 Column이 된다.
    // 사용이유는, 기본값 외에 추가로 변경옵션이 있으면 사용한다. (여기서는 notnull도 줬네.)
    // String 의 경우 VARCHAR(255)가 기본인데, 이걸 500으로 늘리고싶다거나, 타입을 TEXT로 변경하고 싶거나.. 등에 사용된다.
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String author;

    @Builder // Lombok. 이 클래스의 Builder Pattern Class를 생성한다. -> 어디서 쓰지?
    // 지금처럼 생성자 상단에 선언 시 생성자에 포함된 필드만 빌더에 포함한다.
    public Posts(String title, String content, String author){
        this.title = title;
        this.content = content;
        this.author = author;
    }

    // 서비스 구축 초기단계에서는 테이블 설계가 빈번하게 변경되는데, 롬복이 코드 변경량을 최소화시켜주기 때문에 적극적으로 사용한다.

    /* 현재 이 Posts 클래스에는 Setter가 없다. Beans 규약을 생각하면서 getter/setter를 무작정 생성하는 경우가 있는데,
       그렇게 되면 해당 클래스의 인스턴스 값들이 언제 어디서 변해야 하는 지 코드상으로 명확하게 구분할 수 없어, 차후 기능변경시 정말 복잡해진다.
       그래서 Entity Class 에서는 절대 Setter를 만들지 않는다. 대신, 해당 필드값을 진짜 변경해야되면 그 목적과 의도를 나타낼 수 있는 메소드를 추가한다.
       예를들어, 주문취소 메서드를 만들시, setStatus(flase) 가 아니라, cancelOrder() 같이 만들어서 명시한다는 뜻이다.

       그럼, Setter가 없는데 어떻게 값을 채워서 DB에 반영시킬까?
           - 기본적으로 생성자를 통해 최종값을 채우고 DB에 삽입한다.
           - 변경이 필요한 경우 해당 이벤트에 맞는 public 메소드를 호출하여 변경하는 것을 전제로 한다. (cancelOrder() 같이..)
           - 여기서는 생성자 대신 Builder Class를 이용했다.
                - 생성자나 Builder나 생성시점에 값을 채우는 역할은 똑같다.
                - 다만, 생성자는 채울 필드를 명확히 지정할 수 없다.
                - 예를 들어, (a, b) 순서로 받아야 하는데, (b, a)로 생성해도 문제를 찾을 수 없다.
                - 하지만 빌더의 경우 명확하게 인지할 수 있다. -> 솔직히 잘 모르겠다.
                    - Example.builder().a(a).b(b).build();
    */

    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }
}
