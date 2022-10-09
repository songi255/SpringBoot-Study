package com.springboot.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // SpringBoot의 자동설정, Bean 읽기 및 생성 모두 자동으로 설정
// 특히, 이 어노테이션 있는 위치부터 설정을 읽기에, 이 클래스는 항상 프로젝트 최상단에 위치해야 함.
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args); // run은 내장 WAS를 실행함
        /* 내장 WAS란?
            별도로 외부에 WAS를 두지 않고, App 실행 시 내부에서 WAS를 실행하는 것.
                - 항상 서버에 Tomcat을 설치할 필요가 없음
                - SpringBoot로 만들어진 Jar 파일을 실행만 하면 됨
            꼭 Boot 에서만 내장 WAS 를 사용할 수 있는 것은 아니지만, 부트에서는 내장 WAS의 사용을 권한다.
                - 이유는 간단. 언제 어디서나 같은 환경의 SpringBoot를 배포할 수 있기 때문
            외장 AWS를 쓰는 경우...
                - 모든 서버는 WAS의 종류, 버전, 설정을 일치시켜야 함.
                    - 버전업이라도 수행하는 경우... 서버가 막 30대 넘어가고 그런다면?.. 매우 힘들어진다.
                - 그래서 많은 회사가 내장 WAS를 사용하도록 전환하고 있다.
            성능이슈는?
                - 임상적으로 문제가 없었다고 한다.
        * */
    }
}
