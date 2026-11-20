package org.example.demo.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;


@Entity
@Table(name = "uzenetek")
public class Uzenet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A név megadása kötelező!")
    @Size(max = 100, message = "A név legfeljebb 100 karakter lehet!")
    @Column(nullable = false, length = 100)
    private String nev;

    @NotBlank(message = "Az e-mail-cím megadása kötelező!")
    @Email(message = "Érvénytelen e-mail-cím!")
    @Size(max = 150, message = "Az e-mail-cím túl hosszú!")
    @Column(nullable = false, length = 150)
    private String email;

    @NotBlank(message = "Az üzenet megadása kötelező!")
    @Size(max = 2000, message = "Az üzenet legfeljebb 2000 karakter lehet!")
    @Column(nullable = false, length = 2000)
    private String szoveg;

    @Column(nullable = false)
    private LocalDateTime datum;
    public Uzenet() {
    }

    @PrePersist
    public void mentesElott() {
        if (datum == null) {
            datum = LocalDateTime.now();
        }
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNev() {
        return nev;
    }
    public void setNev(String nev) {
        this.nev = nev;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getSzoveg() {
        return szoveg;
    }
    public void setSzoveg(String szoveg) {
        this.szoveg = szoveg;
    }
    public LocalDateTime getDatum() {
        return datum;
    }
    public void setDatum(LocalDateTime datum) {
        this.datum = datum;
    }
}