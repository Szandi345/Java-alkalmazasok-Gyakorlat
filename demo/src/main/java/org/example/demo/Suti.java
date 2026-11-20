package org.example.demo;
import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "suti")
public class Suti {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "nev")
    private String név;
    @Column(name = "tipus")
    private String tipus;
    @Column(name = "dijazott")
    private Boolean dijazott;

    @OneToMany(mappedBy = "suti")
    private List<Ar> arak = new ArrayList<>();

    @OneToMany(mappedBy = "suti")
    private List<Tartalom> tartalmak = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public List<Ar> getArak() {
        return arak;
    }

    public void setArak(List<Ar> arak) {
        this.arak = arak;
    }

    public List<Tartalom> getTartalmak() {
        return tartalmak;
    }

    public void setTartalmak(List<Tartalom> tartalmak) {
        this.tartalmak = tartalmak;
    }
}
