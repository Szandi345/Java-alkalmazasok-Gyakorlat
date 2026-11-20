package org.example.demo.controller;
import org.example.demo.Suti;
import org.example.demo.repository.SutiRepo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.stream.StreamSupport;

@Controller
public class CrudController {
    private final SutiRepo sutiRepo;
    public CrudController(SutiRepo sutiRepo) {
        this.sutiRepo = sutiRepo;
    }

    // READ - Sütemények listázása
    @GetMapping("/crud")
    public String crud(Model model) {
        model.addAttribute(
                "sütik",
                sutiRepo.findAll()
        );
        return "crud";
    }

    // CREATE - Új sütemény űrlap
    @GetMapping("/uj")
    public String uj(Model model) {
        model.addAttribute("suti", new Suti());
        model.addAttribute("tipusok", tipusokLekerese());
        return "new";
    }

    // CREATE - Új sütemény mentése
    @PostMapping("/uj")
    public String ujMentes(
            @ModelAttribute("suti") Suti suti,
            Model model) {
        if (suti.getNév() == null ||
                suti.getNév().isBlank() ||
                suti.getTipus() == null ||
                suti.getTipus().isBlank() ||
                suti.getDijazott() == null ||
                !tipusokLekerese().contains(suti.getTipus())) {
            model.addAttribute(
                    "hiba",
                    "Kérlek, töltsd ki helyesen az összes mezőt!"
            );
            model.addAttribute("tipusok", tipusokLekerese());
            return "new";
        }
        sutiRepo.save(suti);
        return "redirect:/crud?siker=letrehozva";
    }

    // UPDATE - Szerkesztési űrlap
    @GetMapping("/edit/{id}")
    public String szerkesztes(
            @PathVariable int id,
            Model model) {
        Suti suti = sutiRepo.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "A sütemény nem található!"
                        )
                );
        model.addAttribute("suti", suti);
        return "edit";
    }

    // UPDATE - Módosítás mentése
    @PostMapping("/edit")
    public String szerkesztesMentes(
            @ModelAttribute("suti") Suti adatok,
            Model model) {
        Suti suti = sutiRepo.findById(adatok.getId())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "A sütemény nem található!"
                        )
                );
        if (adatok.getNév() == null ||
                adatok.getNév().isBlank() ||
                adatok.getTipus() == null ||
                adatok.getTipus().isBlank()) {
            model.addAttribute("suti", adatok);
            model.addAttribute(
                    "hiba",
                    "A név és a típus megadása kötelező!"
            );
            return "edit";
        }

        // A meglévő entitást módosítjuk,
        // így a kapcsolódó adatok megmaradnak.
        suti.setNév(adatok.getNév());
        suti.setTipus(adatok.getTipus());
        suti.setDijazott(adatok.getDijazott());
        sutiRepo.save(suti);
        return "redirect:/crud";
    }

    // DELETE - Sütemény törlése
    @PostMapping("/delete/{id}")
    public String torles(@PathVariable int id) {
        Suti suti = sutiRepo.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "A sütemény nem található!"
                        )
                );

        // Kapcsolódó árak és tartalmak ellenőrzése
        boolean vanAr = StreamSupport.stream(
                suti.getArak().spliterator(), false
        ).findAny().isPresent();
        boolean vanTartalom = StreamSupport.stream(
                suti.getTartalmak().spliterator(), false
        ).findAny().isPresent();
        if (vanAr || vanTartalom) {
            return "redirect:/crud?torlesHiba=true";
        }
        sutiRepo.delete(suti);
        return "redirect:/crud";
    }

    //Segéd metodus
    private java.util.List<String> tipusokLekerese() {
        return java.util.stream.StreamSupport
                .stream(sutiRepo.findAll().spliterator(), false)
                .map(Suti::getTipus)
                .filter(t -> t != null && !t.isBlank())
                .distinct()
                .sorted()
                .toList();
    }
}