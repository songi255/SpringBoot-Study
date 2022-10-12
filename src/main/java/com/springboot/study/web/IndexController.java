package com.springboot.study.web;

import com.springboot.study.service.posts.PostsService;
import com.springboot.study.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// 페이지에 관련된 컨트롤러는 모두 이 IndexController를 사용한다.

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("posts", postsService.findAllDesc()); // server template engine에서 사용할 수 있는 객체를 저장할 수 있다.
        // postsService.findAllDesc()의 결과값을 index.mustache에 전달한다.
        return "index";
        // 문자열 하나만 반환한다. 무슨 의미일까?
        // 첫째로, return 값은 ViewReslover가 처리한다.
        // 둘째로, mustache starter에 의해 경로와 확장자가 자동으로 추가되어 리턴된다. 즉, src/main/resources/template/index.mustache로 바뀐다.
        // ViewResolver는 URL 요청결과를 전달할 타입과 값을 지정하는 관리자격으로 볼 수 있다.
    }

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
