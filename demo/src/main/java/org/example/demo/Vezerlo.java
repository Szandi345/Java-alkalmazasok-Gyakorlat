package org.example.demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class Vezerlo {
    // Dependency injection:
    @Autowired private SutiRepo sutiRepo;
    @Autowired private ArRepo arRepo;
    @Autowired private TartalomRepo tartalomRepo;
    @GetMapping("/1")
    public String Fooldal(Model model) {
        String str = A();
        model.addAttribute("str", str);
        return "index";
    }
    String A(){
        String str="";
        for(Suti süti: sutiRepo.findAll()){
            str+=süti.getId()+"; "+süti.getNév()+"; "+süti.getTipus()+"; "
            +süti.getDijazott();
            str+="<br>";
        }
        str+="<br>";
        for(Ar ár: arRepo.findAll()){
            str+=ár.getId()+"; "+ár.getSuti().getNév()+"; "+ár.getErtek()+"; "+ár.getEgyseg();
            str+="<br>";
        }
        str+="<br>";
        for(Tartalom tartalom: tartalomRepo.findAll()){
            str+=tartalom.getId()+"; "+tartalom.getSuti().getNév()+"; "+tartalom.getMentes();
            str+="<br>";
        }
        return str;
    }
}