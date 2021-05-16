package com.example.enestezcan;

public class Soru implements java.io.Serializable {


    private Integer soruID;
    private Integer sinavID;
    private String soruMetni;
    private String[] Siklar;
    private Integer dogruSik;
    private Float point;
    private Sinav sinav;
    private String ek;




    public Soru(String soruMetni, String[] siklar, Integer dogruSik, Float puan) {
        this.soruMetni = soruMetni;
        Siklar = siklar;
        this.dogruSik = dogruSik;
        this.point = puan;
        this.ek = "";
    }

    public Soru(Sinav sinav) {
        this.soruMetni = "";
        Siklar = new String[]{"", "","","",""};
        this.dogruSik = 0;
        this.point = 0f;
        this.ek = "";
    }

    public String getSoruMetni() {
        return soruMetni;
    }

    public void setSoruMetni(String soruMetni) {
        this.soruMetni = soruMetni;
    }


    public void setSiklar(String[] siklar) {
        Siklar = siklar;
    }

    public Integer getDogruSik() {
        return dogruSik;
    }

    public void setDogruSik(Integer dogruSik) {
        this.dogruSik = dogruSik;
    }

    public Float getPoint() {
        return point;
    }

    public void setPoint(Float point) {
        this.point = point;
    }

    public String sikkiAl(int num){
        return Siklar[num];
    }

    public String getSinavIsmi() {
        return sinav.getSinavisim();
    }
    public Integer getSınavZorluk(){
        return sinav.getZorluk();
    }

    public String getEk() {
        return ek;
    }

    public void setEk(String ek) {
        this.ek = ek;
    }

    public String getSiklar(){
        String siklarString ="";
        for(int i=0; i<sinav.getZorluk(); i++){
            char cevapChar = (char)('A'+i);
            siklarString += cevapChar +") "+ this.Siklar[i]+" \n";
        }
        return siklarString;
    }



    public Integer getSinavID() {
        return sinavID;
    }

    public void setSinavID(Integer sinavID) {
        this.sinavID = sinavID;
    }

    public void setSinav(Sinav sinav) {
        this.sinav = sinav;
    }

    public Sinav getSinav() {
        return sinav;
    }

    public Integer getSoruID() {
        return soruID;
    }

    public void setSoruID(Integer soruID) {
        this.soruID = soruID;
    }
}
