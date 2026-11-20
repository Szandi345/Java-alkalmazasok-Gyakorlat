package org.example.demo.controller;
import org.example.demo.entity.Uzenet;
import org.example.demo.repository.UzenetRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@Controller
public class KapcsolatController {

    private final UzenetRepository uzenetRepository;
    public KapcsolatController(UzenetRepository uzenetRepository) {
        this.uzenetRepository = uzenetRepository;
    }

    @GetMapping("/kapcsolat")
    public String kapcsolat(Model model) {
        model.addAttribute("uzenet", new Uzenet());
        return "kapcsolat";
    }

    @PostMapping("/kapcsolat")
    public String uzenetKuldes(
            @Valid @ModelAttribute("uzenet") Uzenet uzenet,
            BindingResult result) {

        if (result.hasErrors()) {
            return "kapcsolat";
        }
        uzenetRepository.save(uzenet);
        return "redirect:/kapcsolat?siker=true";
    }

    @GetMapping("/uzenetek")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String uzenetek(Model model) {
        model.addAttribute(
                "uzenetek",
                uzenetRepository.findAllByOrderByDatumDesc()
        );
        return "uzenetek";
    }
}
