package org.example.demo.controller;
import org.example.demo.entity.User;
import org.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
public class Controller2 {
    private final UserRepository userRepository;
    // passwordEncoder: ebben adjuk majd meg, hogy milyen titkosítási algoritmust használjon a jelszó
// titkosítására. Ezt majd a WebSecurityConfig.java-ban adjuk meg.
// Amíg az nincs kész addig itt hibát jelez.
    private final PasswordEncoder passwordEncoder;
    public Controller2(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }
    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password, Model
            model) {
        if (userRepository.findByUsername(username).isPresent()) {
            model.addAttribute("error","This username is already taken!");
            return "register";
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("USER");
        userRepository.save(user);
// Sikeres regisztráció esetén átirányítjuk a login útvonalra registered paraméterrel.
// A login.html-ben ez van:
// <div th:if="${param.registered}" style="color: green;">Successful registration! You can log in.</div>
        return "redirect:/login?registered";
    }
}