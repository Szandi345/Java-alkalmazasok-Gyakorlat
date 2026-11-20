package org.example.demo;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController


class SutiController {
    private final SutiRestRepository repo;
    SutiController(SutiRestRepository repo) { // Dependency injection kontrollerel
        this.repo = repo;
    }
    @GetMapping("/sutik")
    Iterable<SutiRest> olvasMind() {
        return repo.findAll();
    }
    @GetMapping("/sutik/{id}")
    SutiRest olvasEgy(@PathVariable Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new SutiRestNotFoundException(id)); // majd elkészítjük
    }
    @PostMapping("/sutik")
    SutiRest sutiRestFeltolt(@RequestBody SutiRest ujSutiRest) {
        return repo.save(ujSutiRest);
    }
    @PutMapping("/sutik/{id}")
    SutiRest sutiRestModosit(@RequestBody SutiRest adatSutiRest, @PathVariable Long id) {
        return repo.findById(id)
                .map(a -> { // Lambda kifejezés
                    a.setNév(adatSutiRest.getNév());
                    a.setTipus(adatSutiRest.getTipus());
                    a.setDijazott(adatSutiRest.getDijazott());
                    return repo.save(a);
                })
                .orElseGet(() -> { // ha nincs ilyen id-jű személy, akkor vegye fel új rekordnak
                    adatSutiRest.setId(id);
                    return repo.save(adatSutiRest);
                });
    }
    @DeleteMapping("/sutik/{id}")
    void torolSutiRest(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
