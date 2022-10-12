package com.springboot.study.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts, Long> {
    /* Posts 클래스를 사용하여 DB 접근시켜줄 interface 이다.
        보통 MyBatis 등에서 Dao라고 불리는 DB Layer 접근자이다. (JPA에서는 DAO 대신 Repository 라고 부른다.)
        이렇게 단순히 interface 생성 후 JpaRepository<Entity, PK타입>을 상속하면 기본적인 CRUD가 자동 생성된다.
            - @Repository를 추가할 필요도 없다. 다만 Entity와 Repository는 함께 위치해야 한다. 서로가 없으면 의미가 없으므로...
            - 그래서 나중에 프로젝트가 커져서, 도메인별로 프로젝트를 분리하거나 한다면, Entity와 Repository가 함께 움직여야 하니 domain 패키지에서 함께 관리한다.
     */

    // JPA인데 쿼리를 추가했다. SpringDataJpa에서 제공하지 않는 메서드는 이렇게 쿼리로 작성해도 된다.
    @Query("SELECT p FROM Posts p ORDER BY p.id DESC")
    List<Posts> findAllDesc(); // 사실 이 쿼리는 Jpa 기본메서드로도 해결할 수 있으나 Query가 훨씬 가독성이 좋다.

    /* 규모가 있는 프로젝트에서의 데이터조회는 Join, 복잡한 조건들로 인해 Entity Class 만으로는 처리하기 어렵다.
    그래서 조회용 프레임워크를 추가로 사용한다. 대표적으로 querydsl, jooq, MyBatis 등이 있다.
    조회는 위 3가지 중 하나로 하고, 등록/수정/삭제는 Jpa로 한다.

    개인적으로 querydsl을 추천한다는데, 이유는 다음과 같다.
        - 타입안정성이 보장된다.
            - 단순한 문자열쿼리가 아니고 메소드기반으로 쿼리를 만들기 때문에, 오타나 없는컬럼을 명시할 경우, IDE에서 자동검출 가능하다.
            - MyBatis는 지원하지 않는다.
        - 국내 많은 회사들 (쿠팡, 배민 등 JPA를 적극적으로 사용하는 회사들) 에서는 Querydsl을 적극활용한다.
        - 레퍼런스가 많다.



    */

}
