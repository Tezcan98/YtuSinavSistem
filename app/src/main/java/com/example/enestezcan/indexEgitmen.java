package com.example.enestezcan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

public class indexEgitmen extends AppCompatActivity {
    TextView isim,kullaniciAdi;
    List<Sinav> sinavList;
    List<Soru> soruList;

    RecyclerView recyclerView;
    Button sinavOlustur, degistir;
    ImageView pp;

    DatabaseHelper db;
    Boolean sinavGoruntuleme = true;

    SınavlistAdapter sinavAdapter;
    SoruListAdapter soruAdapter;
    Kullanici kullanici;

    private void listeSinavlaDoldur(){
        sinavAdapter = new SınavlistAdapter(this, sinavList);
        recyclerView.setAdapter(sinavAdapter);
    }
    private void listeSorularlaDoldur(){
        soruAdapter = new SoruListAdapter(this, soruList);
        recyclerView.setAdapter(soruAdapter);
    }
    public Bitmap getImage(String image_path) {
        try {
            File file= new File(image_path);
            Bitmap image = BitmapFactory.decodeFile(file.getAbsolutePath());
            return image;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);


        Intent intent = getIntent();
        kullanici = (Kullanici) intent.getSerializableExtra("kullanici");

        recyclerView = (RecyclerView) findViewById(R.id.sorulisteRecycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        db = new DatabaseHelper(this);
        sinavList = db.sinavlariDondur(kullanici);
        soruList = db.kullanicininSorulari(kullanici);


        for(int i=0; i<soruList.size();i++){
            Soru soru = soruList.get(i);
            for(Sinav sinav:sinavList){
                if (sinav.getSinavID() == soru.getSinavID()){
                    soru.setSinav(sinav);
                    soruList.set(i, soru);
                }
            }
        }

        listeSinavlaDoldur();

        isim = findViewById(R.id.isim);
        kullaniciAdi = findViewById(R.id.kullaniciAdi);
        sinavOlustur = findViewById(R.id.s_olustur);
        degistir = findViewById(R.id.degistir);
        pp = findViewById(R.id.pp);


        String image_path = kullanici.getImagePath();
        pp.setImageBitmap(getImage(image_path));

        isim.setText(kullanici.getName());
        kullaniciAdi.setText(kullanici.getEmail());


        sinavOlustur.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), SinavOlustur.class);
                intent.putExtra("kullanici" , (Serializable)kullanici);
                startActivity(intent);
            }
        });
        degistir.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(sinavGoruntuleme){
                    degistir.setText("SINAVLARI GORUNTULE");
                    sinavGoruntuleme = false;
                    listeSorularlaDoldur();
                }else {
                    degistir.setText("SORULARI GORUNTULE");
                    sinavGoruntuleme = true;
                    listeSinavlaDoldur();

                }

            }
        });
    }
}