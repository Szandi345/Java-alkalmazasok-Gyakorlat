package org.example.demo;
import jakarta.persistence.*;

@Entity
@Table(name = "suti")
public class SutiRest {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY) // AUTO_INCREMENT
    private Long id;
    @Column(name = "nev")
    private String név;
    @Column(name = "tipus")
    private String tipus;
    @Column(name = "dijazott")
    private Boolean dijazott;

    public SutiRest(String név, String tipus, Boolean dijazott) {
        this.név = név;
        this.tipus = tipus;
        this.dijazott = dijazott;
    }

    public SutiRest() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getNév() {
        return név;
    }

    public void setNév(String név) {
        this.név = név;
    }

    public String getTipus() {
        return tipus;
    }

    public void setTipus(String tipus) {
        this.tipus = tipus;
    }

    public Boolean getDijazott() {
        return dijazott;
    }

    public void setDijazott(Boolean dijazott) {
        this.dijazott = dijazott;
    }
}