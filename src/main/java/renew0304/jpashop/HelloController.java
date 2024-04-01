package renew0304.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {
    @GetMapping("hello")
    public String hello(Model model){
        // model로 "data" 라는 키값으로 "hello"라는 벨류값을 넘긴다.
        model.addAttribute("data","hello!");
        // return "hello" ->  templates 이름
        return "hello";
    }
}
