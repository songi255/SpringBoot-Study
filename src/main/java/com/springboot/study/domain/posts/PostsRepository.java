package com.springboot.study.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository extends JpaRepository<Posts, Long> {
    /* Posts 클래스를 사용하여 DB 접근시켜줄 interface 이다.
        보통 MyBatis 등에서 Dao라고 불리는 DB Layer 접근자이다. (JPA에서는 DAO 대신 Repository 라고 부른다.)
        이렇게 단순히 interface 생성 후 JpaRepository<Entity, PK타입>을 상속하면 기본적인 CRUD가 자동 생성된다.
            - @Repository를 추가할 필요도 없다. 다만 Entity와 Repository는 함께 위치해야 한다. 서로가 없으면 의미가 없으므로...
            - 그래서 나중에 프로젝트가 커져서, 도메인별로 프로젝트를 분리하거나 한다면, Entity와 Repository가 함께 움직여야 하니 domain 패키지에서 함께 관리한다.
     */


}
