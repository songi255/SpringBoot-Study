package com.springboot.study.web;

import com.springboot.study.web.dto.HelloResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController // Controller를 JSON반환 Controller로 만들어준다.
// 예전에는 @ResponseBody를 각 메소드마다 선언했던 것을 한번에 사용할 수 있게 해준다고 생각하면 된다.
public class HelloController {

    @GetMapping("/hello") // Get 요청과 매핑되는 API를 만든다.
    // 예전에는 @RequestMapping(method = RequestMethod.GET) 으로 사용했었다. 머.. 여튼 이 프로젝트는 /hello로 요청이 오면 문자열 hello를 반환하는 기능을 가진다.
    public String hello(){
        return "hello";
    }

    @GetMapping("/hello/dto")
    public HelloResponseDto helloDto(@RequestParam("name") String name, @RequestParam("amount") int amount){ // @RequestParam은 외부에서 API로 넘긴 parameter를 가져온다.
        return new HelloResponseDto(name, amount); // lombok에 의해 생성자가 잘 생성되는 것을 테스트코드를 통해 이미 확인했다!!!
    }
}
