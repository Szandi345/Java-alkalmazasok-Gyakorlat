package org.example.demo;
import org.example.demo.repository.ArRepo;
import org.example.demo.repository.SutiRepo;
import org.example.demo.repository.TartalomRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Vezerlo {

    @Autowired
    private SutiRepo sutiRepo;

    @Autowired
    private ArRepo arRepo;

    @Autowired
    private TartalomRepo tartalomRepo;

    @GetMapping({"/adatbazis", "/1"})
    public String adatbazis(Model model) {
        var sutik = sutiRepo.findAll();
        var arak = arRepo.findAll();
        var tartalmak = tartalomRepo.findAll();
        System.out.println("Sütemények száma: " +
                java.util.stream.StreamSupport.stream(sutik.spliterator(), false).count());
        System.out.println("Árak száma: " +
                java.util.stream.StreamSupport.stream(arak.spliterator(), false).count());
        System.out.println("Tartalmak száma: " +
                java.util.stream.StreamSupport.stream(tartalmak.spliterator(), false).count());
        model.addAttribute("sutik", sutik);
        model.addAttribute("arak", arak);
        model.addAttribute("tartalmak", tartalmak);
        return "index";
    }
}