package com.springboot.study.web;

import com.springboot.study.config.auth.LoginUser;
import com.springboot.study.config.auth.dto.SessionUser;
import com.springboot.study.service.posts.PostsService;
import com.springboot.study.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

// 페이지에 관련된 컨트롤러는 모두 이 IndexController를 사용한다.

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
    private final HttpSession httpSession;

    /*
    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("posts", postsService.findAllDesc()); // server template engine에서 사용할 수 있는 객체를 저장할 수 있다.
        // postsService.findAllDesc()의 결과값을 index.mustache에 전달한다.

        // 세션을 가져오는 부분을 좀 더 개선할 수는 없을까? index() 외에 다른 Controller와 Method에서도 세션값이 필요할 수도 있다. 그때마다 이렇게 가져올 수는 없다.
        // 그래서 세션을 메소드 인자로 받을 수 있도록, 어노테이션으로 변경해보겠다.
        SessionUser user = (SessionUser) httpSession.getAttribute("user"); // 앞서, CustomOAuth2UserService에서 로그인 성공시 세션에 SessionUser 저장하고록 했다.
        if (user != null){ // 세션에 저장된 값이 있을 때만 model에 userName으로 등록
            model.addAttribute("userName", user.getName()); // 즉, 성공 시 session.getAttribute("user")에서 값을 가져올 수 있다.
        }

        return "index";
        // 문자열 하나만 반환한다. 무슨 의미일까?
        // 첫째로, return 값은 ViewReslover가 처리한다.
        // 둘째로, mustache starter에 의해 경로와 확장자가 자동으로 추가되어 리턴된다. 즉, src/main/resources/template/index.mustache로 바뀐다.
        // ViewResolver는 URL 요청결과를 전달할 타입과 값을 지정하는 관리자격으로 볼 수 있다.
    }
    */

    // Annotaion 으로 반복되는 부분을 대체해보자!
    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user){ // 걍 Annotaion을 이용해서 Parameter에서 세션을 바로 받고 있다.
        model.addAttribute("posts", postsService.findAllDesc());

        if (user != null){
            model.addAttribute("userName", user.getName());
        }
        return "index";
    }

    // 여기서 더 개선할 수는 없을까? 현재는 애플리케이션 재시작하면 로그인이 풀린다. 세션정보가 내장톰캣 (h2)에 저장되기 때문이다. 배포할 때마다 톰캣은 재시작된다.
    // 끝이아니다. 2대이상의 서버라면 톰캣마다 세션동기화도 필요하다. 그래서 현업에서는 세션저장소를 다음 3개 중 하나를 선택한다.
    //   1. tomcat 세션 사용한다. -> 지금 방식이고, 별다른 설정하지 않으면 기본적으로 선택되는 방식이다. 세션공유를 위해 추가 설정 필요하다.
    //   2. MySQL같은 DB를 세션저장소로 사용한다. -> 여러 WAS간 공용세션 사용하는 가장 쉬운 방법. but 로그인마다 DB IO 발생하여 성능이슈가 생길 수 있다.
    //      - 보통 로그인 요청이 많이 없는 backoffice, 사내 시스템 용도에서 사용한다.
    //   3. Redis, Memcached 같은 Memory DB를 세션저장소로 사용한다.
    //      - B2C 에서 가장 많이 사용하는 방식이다.
    //      - 실제 서비스로 사용하기 위해서는 Embedded Redis 같은 방식 말고 외부메모리서버가 필요하다. -> 그니까.. 로그인을 위한 memory db를 따로 운영한다 이거지..

    // 여기서는 2번째 방법을 사용하곘다. 설정이 간단하고, 사용자가 많지 않으며, 비용절감을 위해서이다.
    // 이후 AWS에 배포하고 운영한다면 redis같은 memeory DB는 부담스럽다. elastic cache에 별도로 사용료를 지불해야 하기 때문이다. 서비스가 커진다면 고려해보라.
    // 우선 spring-session-jdbc를 build.gradle에 추가한다.
    // 이후, application.properties 에 세션저장소를 jdbc로 선택하도록 한다.
    // 이제 h2-console 에서 확인해보면 세션을 위한 테이블 2개(SPRING_SESSION, SPRING_SESSION_ATTRIBUTES) 가 생성된 것을 볼 수 있다.
    // JPA로 인해 세션테이블이 자동생성되었으므로 별도로 해야 할 일은 없다.
    // 물론 아직까지 H2를 사용중이므로 재시작하면 세션이 풀리는건 똑같다. 하지만 이후 AWS 배포시에 AWS의 DB 서비스인 RDS를 사용하게 되니 이때부터는 세션이 풀리지 않는다.

    @GetMapping("/posts/save")
    public String postsSave(){
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model){
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }


}
