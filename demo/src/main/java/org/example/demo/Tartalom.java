package org.example.demo;
import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "tartalom")
public class Tartalom {
    @Id
    private int id;
//    @Column(name = "sutiid")
//    private int sutiid;
    @Column(name = "mentes")
    private String mentes;
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

    public String getMentes() {
        return mentes;
    }

    public void setMentes(String mentes) {
        this.mentes = mentes;
    }

    public Suti getSuti() {
        return suti;
    }

    public void setSuti(Suti suti) {
        this.suti = suti;
    }
}
