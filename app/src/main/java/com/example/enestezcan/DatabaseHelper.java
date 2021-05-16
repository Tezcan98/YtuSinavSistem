package com.example.enestezcan;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 3;

    // Database Name
    private static final String DATABASE_NAME = "sqllite_database";

//    private List<Soru> sorular;


    private final String SINAV_TABLE = "sinav_listesi";
    private static String dersİsmi = "ders_ismi";
    private static String olusturan = "user_id";
    private static String soruSayisi = "soru_sayisi";
    private static String sinavSuresi = "sinav_suresi";
    private static String olusturmaTarihi = "olusturma_tarihi";
    private static String aktifTarihi = "aktif_tarihi";
    private static String derskodu = "ders_kodu";
    private static String sinavTuru = "sinav_turu";
    private static String sorular = "sorular";
    private static String zorluk = "zorluk";


    private final String KULLANICI_TABLE = "kullanici_listesi";
    private String Name = "name";
    private String Email = "email";
    private String password = "password";
    private String profil = "profil";




    private final String SORU_TABLE = "soru_listesi";
    private String soruMetni = "soru_metni";
    private String[] Siklar;
    private String dogruSik = "dogru_sik";
    private String point = "puan";
    private String dosyaeki = "dosya_ek";
    private String sinavID = "sinav_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db=this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_KULLANICI = "CREATE TABLE " + KULLANICI_TABLE + "("
                + olusturan + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Name + " TEXT,"
                + Email + " TEXT NOT NULL UNIQUE," // TODO : PRIMARY KEY
                + password + " TEXT,"
                + profil + " TEXT,"
                + "admin BOOLEAN )";
        db.execSQL(CREATE_KULLANICI);

        String CREATE_SINAV = "CREATE TABLE " + SINAV_TABLE + "("
                + "sinav_id" + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + dersİsmi + " TEXT,"
                + soruSayisi + " INTEGER,"
                + sinavSuresi + " INTEGER,"
                + olusturmaTarihi + " TEXT,"
                + aktifTarihi + " TEXT,"
                + derskodu + " TEXT,"
                + sinavTuru + " TEXT,"
                + zorluk + " INTEGER,"
                + olusturan + " INTEGER," +

                "FOREIGN KEY(user_id) REFERENCES kullanici_listesi(user_id))";
        db.execSQL(CREATE_SINAV);

        String CREATE_SORU = "CREATE TABLE " + SORU_TABLE + "("
                + "soru_id" + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "sinav_id" + " INTEGER,"
                + soruMetni + " INTEGER,"
                + "aSikki  TEXT,"
                + "bSikki TEXT,"
                + "cSikki TEXT,"
                + "dSikki TEXT,"
                + "eSikki TEXT,"
                + dogruSik + " INTEGER,"
                + point + " FLOAT,"
                + dosyaeki + " TEXT," +
                "FOREIGN KEY(sinav_id) REFERENCES sinav_listesi(sinav_id))";
        db.execSQL(CREATE_SORU);
    }

    public void SinavSil(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SINAV_TABLE, "sinav_id = '"+String.valueOf(id) +"'",null);
        db.delete(SORU_TABLE, "sinav_id = '"+String.valueOf(id) +"'",null);
        db.close();
    }
 /// class olarak ver parametreyi
    public Long sinavEkle(Sinav sinav) {

        String dersİsmi = sinav.getDersİsmi();
        Integer soruSayi = sinav.getSoruSayisi();
        Integer olusturanID = sinav.getOlusturan().getKullaniciId();
        Integer sinavSuresi = sinav.getSinavSuresi();
//        String aktifTarihi = sinav.getAktifTarihi();
        String dersKodu = sinav.getDersKodu();
        String sinavTuru = sinav.getSinavTuru();
        Integer soruZorluk = sinav.getZorluk();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valuesIntoSınavTable = new ContentValues();
        valuesIntoSınavTable.put(this.dersİsmi, dersİsmi);
        valuesIntoSınavTable.put(olusturan, olusturanID);
        valuesIntoSınavTable.put(this.sinavSuresi, sinavSuresi);
//        valuesIntoSınavTable.put(this.aktifTarihi, aktifTarihi);
        valuesIntoSınavTable.put(this.olusturmaTarihi, new Date().toString());
        valuesIntoSınavTable.put(this.derskodu, dersKodu);
        valuesIntoSınavTable.put(this.sinavTuru, sinavTuru);
        valuesIntoSınavTable.put(this.soruSayisi, soruSayi);
        valuesIntoSınavTable.put(this.zorluk, soruZorluk);


        return db.insert(SINAV_TABLE, null, valuesIntoSınavTable);

    }

    public void soruEkle(Integer sinavID, List<Soru> sorular) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SORU_TABLE, "sinav_id = ?", new String[] { String.valueOf(sinavID) });

        for( Soru sorum: sorular){
            ContentValues valuesIntoSoruTable = new ContentValues();
            valuesIntoSoruTable.put("sinav_id", sinavID);
            valuesIntoSoruTable.put(soruMetni, sorum.getSoruMetni());
            valuesIntoSoruTable.put("aSikki", sorum.sikkiAl(0));
            valuesIntoSoruTable.put("bSikki", sorum.sikkiAl(1));
            valuesIntoSoruTable.put("cSikki", sorum.sikkiAl(2));
            valuesIntoSoruTable.put("dSikki", sorum.sikkiAl(3));
            valuesIntoSoruTable.put("eSikki", sorum.sikkiAl(4));
            valuesIntoSoruTable.put( dogruSik, sorum.getDogruSik());
            valuesIntoSoruTable.put( point, sorum.getPoint());
            valuesIntoSoruTable.put( dosyaeki, sorum.getEk());

            db.insert(SORU_TABLE, null, valuesIntoSoruTable);
        }
        db.close();
    }

    public Long kullaniciOlustur(Kullanici yeniKullanici, Boolean admin){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues kullaniciVerileri = new ContentValues();

        kullaniciVerileri.put(this.Name, yeniKullanici.getName());
        kullaniciVerileri.put(this.Email, yeniKullanici.getEmail());
        kullaniciVerileri.put(this.password, yeniKullanici.getPassword());
        kullaniciVerileri.put("admin", admin);
        kullaniciVerileri.put(profil, yeniKullanici.getImagePath());
        try {
            return db.insert(KULLANICI_TABLE, null, kullaniciVerileri);
        }
        catch (Exception e){
            return -1L;
        }

    }
    public Kullanici girisYap(String Email, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM '" + KULLANICI_TABLE +"' WHERE email='" +Email +"'";
        Cursor cursor = db.rawQuery(selectQuery, null);

        Kullanici girisYapan = null;
        if (cursor.moveToFirst()){
                Integer id = cursor.getInt(0);
                String uName = cursor.getString(1);
                String uEmail = cursor.getString(2);
                String upassword = cursor.getString(3);
            String imagePath = cursor.getString(4);

            Boolean admin = (cursor.getInt(5) == 1);

                if (admin)
                    girisYapan = new Egitmen(id,uName, uEmail, upassword, imagePath);
                else
                    girisYapan = new Ogrenci(id,uName, uEmail, upassword, imagePath);
                if(!upassword.equals(password))
                    return null;
        }
        cursor.close();
        db.close();
        // return kitap liste
        return girisYapan;
    }

    public List<Sinav> sinavlariDondur( Kullanici user){

        List<Sinav> sinavList = new ArrayList<Sinav>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM '" + SINAV_TABLE + "' WHERE user_id='" + user.getKullaniciId()+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            do {
            // Passing values
                Integer id = cursor.getInt(0);
                String dersİsmi = cursor.getString(1);
                Integer soruSayisi = cursor.getInt(2);
                Integer sinavSuresi = cursor.getInt(3);
                String olusturmaTarihi = cursor.getString(4);
                String aktifTarihi = cursor.getString(5);
                String derskodu = cursor.getString(6);
                String sinavTuru = cursor.getString(7);
                Integer zorluk = cursor.getInt(8);

                Sinav dummySinav = new Sinav(dersİsmi, user, olusturmaTarihi, sinavSuresi, aktifTarihi, derskodu, sinavTuru, zorluk);
                dummySinav.setSinavID(id);
                sinavList.add(dummySinav);

            // Do something Here with values
            } while(cursor.moveToNext());
        }

        return sinavList;
    }

    public List<Soru> sorularDon(Cursor cursor){
        List<Soru> soruList = new ArrayList<Soru>();
        if (cursor.moveToFirst()){
            do {
                // Passing values
                Integer id = cursor.getInt(0);
                Integer sinav_id = cursor.getInt(1);
                String soruMetni = cursor.getString(2);
                String a_sikki = cursor.getString(3);
                String b_sikki = cursor.getString(4);
                String c_sikki = cursor.getString(5);
                String d_sikki = cursor.getString(6);
                String e_sikki = cursor.getString(7);
                Integer dogru = cursor.getInt(8);
                Float puan = cursor.getFloat(9);
                String dosya_ek = cursor.getString(10);

                String[] siklar =  new String[] {a_sikki,b_sikki,c_sikki,d_sikki,e_sikki};
                Soru dummySoru = new Soru(soruMetni, siklar , dogru, puan);
                dummySoru.setSoruID(id);
                dummySoru.setSinavID(sinav_id);
                dummySoru.setEk(dosya_ek);
                soruList.add(dummySoru);

                Log.d("238/********", soruMetni);
                // Do something Here with values
            } while(cursor.moveToNext());
        }
        return soruList;
    }

    public List<Soru> sinavinSorulari(Integer sinavID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM '" + SORU_TABLE + "' WHERE sinav_id='" + sinavID+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        return sorularDon(cursor);
    }
    public List<Soru> kullanicininSorulari(Kullanici user){
        SQLiteDatabase db = this.getReadableDatabase();
        String join_query = "SELECT * FROM "+ SORU_TABLE +" soru INNER JOIN "+SINAV_TABLE+" sinav ON sinav.sinav_id = soru.sinav_id WHERE sinav.user_id = '"+ user.getKullaniciId()+"'";
        Cursor cursor = db.rawQuery(join_query, null);
        return sorularDon(cursor);
    }

    public void sinavDuzenle(Sinav sinav) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valuesUpdateSınavTable = new ContentValues();
        valuesUpdateSınavTable.put(this.dersİsmi, sinav.getDersİsmi());
        valuesUpdateSınavTable.put(this.sinavSuresi, sinav.getSinavSuresi());
        valuesUpdateSınavTable.put(this.derskodu, sinav.getDersKodu());
        valuesUpdateSınavTable.put(this.sinavTuru, sinav.getSinavTuru());
        valuesUpdateSınavTable.put(this.zorluk, sinav.getZorluk());

        db.update(SINAV_TABLE, valuesUpdateSınavTable, "sinav_id = '" + sinav.getSinavID().toString() +"'" ,null );
        db.close();
    }
    public void soruDuzenle(Soru sorum) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valuesUpdateSınavTable = new ContentValues();
        valuesUpdateSınavTable.put("sinav_id", sorum.getSinavID());
        valuesUpdateSınavTable.put(soruMetni, sorum.getSoruMetni());
        valuesUpdateSınavTable.put("aSikki", sorum.sikkiAl(0));
        valuesUpdateSınavTable.put("bSikki", sorum.sikkiAl(1));
        valuesUpdateSınavTable.put("cSikki", sorum.sikkiAl(2));
        valuesUpdateSınavTable.put("dSikki", sorum.sikkiAl(3));
        valuesUpdateSınavTable.put("eSikki", sorum.sikkiAl(4));
        valuesUpdateSınavTable.put( dogruSik, sorum.getDogruSik());
        valuesUpdateSınavTable.put( point, sorum.getPoint());

        db.update(SORU_TABLE, valuesUpdateSınavTable, "soru_id = '" + sorum.getSoruID() +"'" ,null );
        db.close();
    }




    public void sinavBaslat( Integer ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        //exec update
        db.close();
    }
    public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(KULLANICI_TABLE, null, null);
        db.delete(SINAV_TABLE, null, null);
        db.delete(SORU_TABLE, null, null);
        db.close();
    }
    public void dropTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+KULLANICI_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+SINAV_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+SORU_TABLE);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }


}