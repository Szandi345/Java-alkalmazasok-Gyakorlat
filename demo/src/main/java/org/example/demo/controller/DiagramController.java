package org.example.demo.controller;
import org.example.demo.Suti;
import org.example.demo.repository.SutiRepo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Map;
import java.util.TreeMap;

@Controller
public class DiagramController {
    private final SutiRepo sutiRepo;
    public DiagramController(SutiRepo sutiRepo) {
        this.sutiRepo = sutiRepo;
    }
    @GetMapping("/diagram")
    public String diagram(Model model) {
        Map<String, Long> tipusDarab = new TreeMap<>();
        for (Suti suti : sutiRepo.findAll()) {
            String tipus = suti.getTipus();
            if (tipus == null || tipus.isBlank()) {
                tipus = "Ismeretlen";
            }
            tipusDarab.merge(tipus, 1L, Long::sum);
        }
        model.addAttribute(
                "tipusok",
                tipusDarab.keySet()
        );
        model.addAttribute(
                "darabszamok",
                tipusDarab.values()
        );
        return "diagram";
    }
}