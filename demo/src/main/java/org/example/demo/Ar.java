package org.example.demo;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "ar")
public class Ar {
    @Id
    private int id;
//    @Column(name = "sutiid")
//    private int sutiid;
    @Column(name = "ertek")
    private int ertek;
    @Column(name = "egyseg")
    private String egyseg;
    @ManyToOne
    @JoinColumn(name = "sutiid")
    private Suti suti;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public int getSutiid() {
//        return sutiid;
//    }
//
//    public void setSutiid(int sutiid) {
//        this.sutiid = sutiid;
//    }

    public int getErtek() {
        return ertek;
    }

    public void setErtek(int ertek) {
        this.ertek = ertek;
    }

    public String getEgyseg() {
        return egyseg;
    }

    public void setEgyseg(String egyseg) {
        this.egyseg = egyseg;
    }

    public Suti getSuti() {
        return suti;
    }

    public void setSuti(Suti suti) {
        this.suti = suti;
    }
}
