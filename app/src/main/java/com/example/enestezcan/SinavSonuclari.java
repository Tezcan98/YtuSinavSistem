package com.example.enestezcan;

import java.util.Date;

public class SinavSonuclari {
    private Sinav girilen;
    private Float not;
    private Date tarih;
    private Integer Dogru;
    private Integer Yanlis;
    private Boolean Aciklandi;

    public SinavSonuclari(Sinav girilen, Float not, Date tarih, Integer dogru, Integer yanlis) {
        this.girilen = girilen;
        this.not = not;
        this.tarih = tarih;
        Dogru = dogru;
        Yanlis = yanlis;
        Aciklandi = false;
    }

    public Sinav getGirilen() {
        return girilen;
    }
    public Float getNot() {
        return not;
    }
    public void setNot(Float not) {
        this.not = not;
    }

    public Date getTarih() {
        return tarih;
    }

    public void setTarih(Date tarih) {
        this.tarih = tarih;
    }

    public Integer getDogru() {
        return Dogru;
    }

    public void setDogru(Integer dogru) {
        Dogru = dogru;
    }

    public Integer getYanlis() {
        return Yanlis;
    }

    public void setYanlis(Integer yanlis) {
        Yanlis = yanlis;
    }
    public Boolean getAciklandi() {
        return Aciklandi;
    }
    public void setAciklandi() {
        Aciklandi = true;
    }
}
