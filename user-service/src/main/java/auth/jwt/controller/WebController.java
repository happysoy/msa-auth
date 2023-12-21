package auth.jwt.controller;


import auth.jwt.dto.request.LoginRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user-service")
public class WebController {

    @GetMapping("/join")
    public String joinForm() {
        return "joinForm";
    }
//    @ModelAttribute("joinForm") JoinRequest request 이거 안넘겨줘도 됨

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginRequest request) {
        return "loginForm";
    }

    @GetMapping("/profile")
    public String profileGet() {
        return "profile";
    }

    @GetMapping("/admin")
    public String adminGet() {
        return "admin";
    }
}
