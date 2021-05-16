package com.example.enestezcan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SinavOlustur extends AppCompatActivity {

    Button soruOlustur, sinaviSil, kaydet;
    Sinav thisSinav;
    EditText dersIsmi,dersKodu,sinavSuresi;
    Spinner sinavTuru,zorluk;
    Integer duzenlenenSinavID = -1;
    DatabaseHelper db;
    Kullanici kullanici;
    SharedPrefSinifi sharedpref;

    private void kaydetVeyaGuncelle(){
        String sinavSuresiText = sinavSuresi.getText().toString();
        if (sinavSuresiText.equals("")) {
            Toast.makeText(getApplicationContext(), "Sınav Süresi Belirtilmeli.", Toast.LENGTH_LONG).show();
            return;
        }
        String sinavTur = sinavTuru.getSelectedItem().toString();
        Integer sinavSure = Integer.parseInt(sinavSuresiText);
        Integer sinavZorluk = Integer.parseInt(zorluk.getSelectedItem().toString());
        Log.d("41**",zorluk.getSelectedItem().toString());

        if (duzenlenenSinavID != -1) {
            thisSinav =
                    new Sinav(
                            duzenlenenSinavID,
                            dersIsmi.getText().toString(),
                            sinavSure,
                            dersKodu.getText().toString(),
                            sinavTur,
                            sinavZorluk
                    );
            db.sinavDuzenle(thisSinav);
        }
        else {

            thisSinav = new Sinav(
                    dersIsmi.getText().toString(),
                    kullanici,
                    new Date().toString(),
                    sinavSure,
                    new Date().toString(),
                    dersKodu.getText().toString(),
                    sinavTur,
                    sinavZorluk
            );

            duzenlenenSinavID = db.sinavEkle(thisSinav).intValue();
            thisSinav.setSinavID(duzenlenenSinavID);
            // Son ayarlar varsayılan olarak belirlenir.
            sharedpref.setDefaultSettings(SinavOlustur.this, sinavTur, sinavZorluk, sinavSure);

     }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinav_olustur);
        db = new DatabaseHelper(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        kullanici = (Kullanici)extras.getSerializable("kullanici");
        thisSinav = (Sinav) extras.getSerializable("sinav");

        dersIsmi = findViewById(R.id.dersismi);
        dersKodu = findViewById(R.id.derskodu);
        sinavSuresi = findViewById(R.id.sorupuan);
        sinavTuru = findViewById(R.id.sinavTuru);
        zorluk = findViewById(R.id.zorluk_s);
        kaydet = findViewById(R.id.sinavi_kaydet);
        soruOlustur = findViewById(R.id.soruOls);
        sinaviSil = findViewById(R.id.sil);




        String[] arraySpinner = new String[] {
                "Vize", "Final", "Arasinav","Bütünleme","Telafi"
        };

        String[] zorlukSpinner = new String[] {
            "2","3","4","5"
        };

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arraySpinner);
        sinavTuru.setAdapter(arrayAdapter);

        ArrayAdapter<String> zorlukAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, zorlukSpinner);
        zorluk.setAdapter(zorlukAdapter);

        if(thisSinav != null){
            duzenlenenSinavID =  thisSinav.getSinavID();

            dersIsmi.setText(thisSinav.getDersİsmi());
            dersKodu.setText(thisSinav.getDersKodu());
            sinavSuresi.setText(thisSinav.getSinavSuresi().toString());
            Integer SinavTuruidx = Arrays.asList(arraySpinner).indexOf(thisSinav.getSinavTuru());
            sinavTuru.setSelection(SinavTuruidx);
            zorluk.setSelection(thisSinav.getZorluk()-2);
            soruOlustur.setText("Soruları düzenle");
            sinaviSil.setVisibility(View.VISIBLE);
        }
        else{
            sinaviSil.setVisibility(View.INVISIBLE);
            List<Object> Defaultsetttings = sharedpref.getDefaultSettings(this);

            Integer SinavTuruidx = Arrays.asList(arraySpinner).indexOf(Defaultsetttings.get(0));
            sinavTuru.setSelection(SinavTuruidx);
            zorluk.setSelection(((Integer) Defaultsetttings.get(1))-2);
            sinavSuresi.setText(Defaultsetttings.get(2).toString());
        }




        kaydet.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                kaydetVeyaGuncelle();
                Toast.makeText(getApplicationContext(), "Sınav Başarı ile kaydedildi.", Toast.LENGTH_LONG).show();

            }
        });


        sinaviSil.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                new AlertDialog.Builder(SinavOlustur.this)
                        .setTitle("Olusturduğunuz Sınav Silinecek")
                        .setMessage("Silmek istediğinize Emin Misiniz ? ")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.SinavSil(duzenlenenSinavID);
                                Toast.makeText(getApplicationContext(), "Sınav başarı ile silindi.", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), indexEgitmen.class);
                                intent.putExtra("kullanici", kullanici);
                                startActivity(intent);

                            }
                        }).setNegativeButton("No", null).show();


            }
        });

        soruOlustur.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), soruOlustur.class);
                kaydetVeyaGuncelle();

                intent.putExtra("sinav", thisSinav);
                intent.putExtra("kullanici", kullanici);
                startActivity(intent);
            }
        });



    }


}