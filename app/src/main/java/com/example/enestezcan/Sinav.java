package com.example.enestezcan;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Sinav implements java.io.Serializable  {
//    private List<Soru> sorular;
    private Integer sinavID;
    private String dersİsmi;
    private Kullanici olusturan;
    private Integer soruSayisi;
    private Integer sinavSuresi;
    private Date olusturmaTarihi, aktifTarihi, sonTarih;
    private String dersKodu; // ogrenci olan kullanicinin aldigi derse gore bi kod mesela blm4020 olan ogrenciler
    private String sinavTuru; // vize/final
//    private DateFormat saat = new SimpleDateFormat("h-m");
//    private DateFormat tarih = new SimpleDateFormat("dd-M-yy");
    private DateFormat tarihsaat = new SimpleDateFormat("dd-M-yy/h.m");
    private String sinavOzelKodu;
    private Boolean Basladi;
    private Integer zorluk;

    public Sinav(String dersİsmi, Kullanici olusturan, String olusturmaTarihi, int sinavSuresi, String aktifTarihi, String dersKodu, String sinavTuru, Integer zorluk) {
        this.dersİsmi = dersİsmi;
        this.olusturan = olusturan;
        this.sinavSuresi = sinavSuresi;
        this.olusturmaTarihi =  new Date();  /// TODO: bakilacak !!!
//        this.sonTarih = aktifTarihi + sinavSuresi;
        this.dersKodu = dersKodu;
        this.sinavTuru = sinavTuru;
        this.soruSayisi = 0;
        Basladi = false;
        this.zorluk = zorluk;
    }

    public Sinav(Integer id, String dersİsmi, int sinavSuresi, String dersKodu, String sinavTuru, Integer zorluk) {
        this.sinavID = id;
        this.dersİsmi = dersİsmi;
        this.sinavSuresi = sinavSuresi;
        this.dersKodu = dersKodu;
        this.sinavTuru = sinavTuru;
        this.zorluk = zorluk;
    }

    public Boolean getBasladi() {
        return Basladi;
    }

    public void baslat() {
        this.aktifTarihi = new Date();
        Basladi = true;
    }

    public Boolean aktifMi(){
        if (new Date().getTime() - this.sonTarih.getTime() > 0)
            return true;
        else
            return false;
    }
    public float olusturmaZamani(){
        return this.olusturmaTarihi.getTime() - new Date().getTime() ;
    }

    public Integer soruEklen() {
        return this.soruSayisi++;
    }

    public String getSinavisim(){ return this.dersİsmi +" - "+ this.sinavTuru;}

    public String getOlusturmaTarihi() {
        return tarihsaat.format(olusturmaTarihi).toString();
    }

    public String getAktifTarihi() {
//        return tarihsaat.format(aktifTarihi).toString();
        return tarihsaat.format(aktifTarihi);
    }

//    public void soruDegis(Integer soruNumarasi, Soru ekliSoru) {
//        sorular.set(soruNumarasi, ekliSoru);
//    }

    public Kullanici getOlusturan() {
        return olusturan;
    }

    public String getDersİsmi() {
        return dersİsmi;
    }

    public void setDersİsmi(String dersİsmi) {
        this.dersİsmi = dersİsmi;
    }

    public void setOlusturan(Kullanici olusturan) {
        this.olusturan = olusturan;
    }

    public Integer getSoruSayisi() {
        return soruSayisi;
    }

    public void setSoruSayisi(Integer soruSayisi) {
        this.soruSayisi = soruSayisi;
    }

    public Integer getSinavSuresi() {
        return sinavSuresi;
    }

    public void setSinavSuresi(Integer sinavSuresi) {
        this.sinavSuresi = sinavSuresi;
    }

    public void setOlusturmaTarihi(Date olusturmaTarihi) {
        this.olusturmaTarihi = olusturmaTarihi;
    }

    public void setAktifTarihi(Date aktifTarihi) {
        this.aktifTarihi = aktifTarihi;
    }

    public Date getSonTarih() {
        return sonTarih;
    }

    public void setSonTarih(Date sonTarih) {
        this.sonTarih = sonTarih;
    }

    public String getDersKodu() {
        return dersKodu;
    }

    public void setKatilmaYetkisi(String katilmaYetkisi) {
        this.dersKodu = katilmaYetkisi;
    }

    public String getSinavTuru() {
        return sinavTuru;
    }

    public Integer getSinavID() {
        return sinavID;
    }

    public void setSinavID(Integer sinavID) {
        this.sinavID = sinavID;
    }

    public Integer getZorluk() {
        return zorluk;
    }

    public String sinavYazisi(){
        return "\t"+this.dersİsmi+ " " + this.sinavTuru+ " \n\tSınav Süresi " + this.sinavSuresi + " dakikadır.\n";

    }
}
