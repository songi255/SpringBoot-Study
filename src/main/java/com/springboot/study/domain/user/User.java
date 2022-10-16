package com.springboot.study.domain.user;

import com.springboot.study.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING)
    /* @Enumerated
     JPA로 DB저장 시 Enum값을 어떤 형태로 저장할 지? 설정.
     기본적으로는 int 숫자로 저장된다. 하지만, DB에서 볼때는 그게 무슨 값인지 알 수 없다.
     그래서 String으로 저장되도록 선언했다.
    */
    @Column(nullable = false)
    private Role role; // Role은 Enum Class로 만들어줬다.

    @Builder
    public User(String name, String email, String picture, Role role) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }

    public User update(String name, String picture){
        this.name = name;
        this.picture = picture;

        return this;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }
}
