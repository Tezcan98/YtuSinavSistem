package com.example.enestezcan;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class soruOlustur extends AppCompatActivity {

    private static final int GALLERY_REQUEST = 100;
    EditText soruMetni, aSikki, bSikki, cSikki, dSikki, eSikki, puan, dosyaEk;
    Button yeni,bitir,kaydet, left, right;
    RadioButton aDogru,bDogru,cDogru,dDogru,eDogru;
    RadioGroup grup;
    List<Soru> sorular;
    TextView soruno;
    Sinav thisSinav;
    Soru incelenenSoru;
    Integer soruNumarasi = 0;
    Integer duzenlenenSinavID;
    DatabaseHelper db;
    Kullanici kullanici;
    Integer zorluk;
    LinearLayout c_layout,d_layout,e_layout;

    Boolean soruGuncellemeMod = false;
    String absolute_path;

    private void soruDegis(Integer soruNumarasi){

        soruno.setText((soruNumarasi+1)+"/"+(sorular.size()));
        Soru dumy = sorular.get(soruNumarasi);

        aSikki.setText(dumy.sikkiAl(0));
        bSikki.setText(dumy.sikkiAl(1));
        cSikki.setText(dumy.sikkiAl(2));
        dSikki.setText(dumy.sikkiAl(3));
        eSikki.setText(dumy.sikkiAl(4));
        soruMetni.setText(dumy.getSoruMetni());
        puan.setText(dumy.getPoint().toString());
        Integer dogruIdx = dumy.getDogruSik()*2;
        ((RadioButton)grup.getChildAt(dogruIdx)).setChecked(true);
        dosyaEk.setText(dumy.getEk());
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soru_olustur);
        Context thiscontext = this;

        Intent intent = getIntent();

        db = new DatabaseHelper(this);

        kullanici = (Kullanici) getIntent().getSerializableExtra("kullanici");
        thisSinav = (Sinav)getIntent().getSerializableExtra("sinav");
        incelenenSoru = (Soru)getIntent().getSerializableExtra("soru");


        db = new DatabaseHelper(this);

        aSikki = findViewById(R.id.a_sikki);
        bSikki = findViewById(R.id.b_sikki);
        cSikki = findViewById(R.id.c_sikki);
        dSikki = findViewById(R.id.d_sikki);
        eSikki = findViewById(R.id.e_sikki);

        aDogru = findViewById(R.id.cevap_a);
        bDogru = findViewById(R.id.cevap_b);
        cDogru = findViewById(R.id.cevap_c);
        dDogru = findViewById(R.id.cevap_d);
        eDogru = findViewById(R.id.cevap_e);
        grup = findViewById(R.id.radiogr);

        aDogru.setChecked(true); //default



        soruMetni = findViewById(R.id.metinMultiLine);
        puan = findViewById(R.id.sorupuan);
        kaydet = findViewById(R.id.kaydet);
        yeni = findViewById(R.id.yeni);
        bitir = findViewById(R.id.bitir);
        dosyaEk = findViewById(R.id.dosyaek);

        left = findViewById(R.id.leftb);
        right = findViewById(R.id.rightb);
        left.setText("<");
        right.setText(">");



        soruno = findViewById(R.id.soruno);


        if( incelenenSoru != null) {
            soruGuncellemeMod = true;
            aSikki.setText(incelenenSoru.sikkiAl(0));
            bSikki.setText(incelenenSoru.sikkiAl(1));
            cSikki.setText(incelenenSoru.sikkiAl(2));
            dSikki.setText(incelenenSoru.sikkiAl(3));
            eSikki.setText(incelenenSoru.sikkiAl(4));
            soruMetni.setText(incelenenSoru.getSoruMetni());
            puan.setText(incelenenSoru.getPoint().toString());
            Integer dogruIdx = incelenenSoru.getDogruSik();
            if (dogruIdx != -1)
                ((RadioButton)grup.getChildAt(dogruIdx)).setChecked(true);
            bitir.setVisibility(View.INVISIBLE);
            left.setVisibility(View.INVISIBLE);
            right.setVisibility(View.INVISIBLE);
            yeni.setVisibility(View.INVISIBLE);
            thisSinav = incelenenSoru.getSinav();

        }else{
            duzenlenenSinavID = thisSinav.getSinavID();
            sorular = new ArrayList<Soru>();
            sorular = db.sinavinSorulari(duzenlenenSinavID);
            if (sorular.isEmpty()){
                incelenenSoru = new Soru(thisSinav);
                sorular.add(incelenenSoru);
            }
            else{
                soruDegis(0);
            }
        }
        zorluk = thisSinav.getZorluk();
        cDogru.setVisibility(View.INVISIBLE);
        cSikki.setVisibility(View.INVISIBLE);
        dDogru.setVisibility(View.INVISIBLE);
        dSikki.setVisibility(View.INVISIBLE);
        eDogru.setVisibility(View.INVISIBLE);
        eSikki.setVisibility(View.INVISIBLE);

        if(zorluk>2){
            cSikki.setVisibility(View.VISIBLE);
            cDogru.setVisibility(View.VISIBLE);
        }
        if(zorluk>3){

            dSikki.setVisibility(View.VISIBLE);
            dDogru.setVisibility(View.VISIBLE);
        }
        if(zorluk>4){
            eSikki.setVisibility(View.VISIBLE);
            eDogru.setVisibility(View.VISIBLE);
        }


        yeni.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                kaydet.performClick();
                soruNumarasi= sorular.size();
                soruno.setText((soruNumarasi+1)+"/"+(sorular.size()+1));
                aSikki.setText("");
                bSikki.setText("");
                cSikki.setText("");
                dSikki.setText("");
                eSikki.setText("");
                soruMetni.setText("");
                puan.setText("");
                grup.clearCheck();

                Soru dumySoru = new Soru(thisSinav);
                sorular.add(dumySoru);
            }
        });
        kaydet.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                int radioButtonID = grup.getCheckedRadioButtonId();
                View radioButton = grup.findViewById(radioButtonID);
                int dogruSik = grup.indexOfChild(radioButton);
                String puanText = puan.getText().toString();
                if (puanText.equals(""))
                    puanText="1";
                Float soruPuani = Float.valueOf(puanText);
                String soruMtn = soruMetni.getText().toString();

                String[] siklar = {aSikki.getText().toString(), bSikki.getText().toString(), cSikki.getText().toString(), dSikki.getText().toString(), eSikki.getText().toString()};


                if (soruGuncellemeMod){
                    incelenenSoru.setDogruSik(dogruSik);
                    incelenenSoru.setPoint(soruPuani);
                    incelenenSoru.setSoruMetni(soruMtn);
                    incelenenSoru.setSiklar(siklar);
                    if (!dosyaEk.getText().toString().isEmpty()) {
                        incelenenSoru.setEk(absolute_path);
                    }


                    db.soruDuzenle(incelenenSoru);

                    Toast.makeText(getApplicationContext(), "Soru Kaydedildi", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), indexEgitmen.class);
                    intent.putExtra("kullanici" , (Serializable)kullanici);
                    startActivity(intent);
                }
                else{
                    incelenenSoru = sorular.get(soruNumarasi);
                    incelenenSoru.setDogruSik(dogruSik);
                    incelenenSoru.setPoint(soruPuani);
                    incelenenSoru.setSoruMetni(soruMtn);
                    incelenenSoru.setSiklar(siklar);
                    if (!dosyaEk.getText().toString().isEmpty()) {
                        incelenenSoru.setEk(absolute_path);
                    }
                    sorular.set(soruNumarasi,incelenenSoru);
                }

                Toast.makeText(getApplicationContext(), "Soru Kaydedildi", Toast.LENGTH_SHORT).show();
            }
        });

        bitir.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                kaydet.performClick();
                db.soruEkle(duzenlenenSinavID, sorular);

                Toast.makeText(getApplicationContext(), "Sinav Veritabanına Kaydedildi", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), indexEgitmen.class);
                intent.putExtra("kullanici" , (Serializable)kullanici);
                startActivity(intent);
            }
        });
        left.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(soruNumarasi > 0)
                    soruNumarasi--;
                else
                    soruNumarasi = sorular.size()-1;
                    soruDegis(soruNumarasi);
            }
        });
        right.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                if(soruNumarasi +1 < sorular.size())
                    soruNumarasi++;
                else
                    soruNumarasi = 0;
                soruDegis(soruNumarasi);

            }
        });
        dosyaEk.setOnTouchListener((view, motionEvent) -> {
            if(motionEvent.getAction() == motionEvent.ACTION_UP){
                Intent intent1 = new Intent();
                intent1.setType("*/*");

                intent1.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent1, "Select Attachment"), GALLERY_REQUEST);
            }
            return false;
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && null != data) {
            try {
                Uri attach_path = data.getData();
                InputStream inputStream = getContentResolver().openInputStream(attach_path);
                String attachFileName = attach_path.getPath().substring(attach_path.getPath().lastIndexOf('/') + 1);
                ContextWrapper cw = new ContextWrapper(getApplicationContext());
                File directory = cw.getDir("attachments", Context.MODE_PRIVATE);
                File storageDir = new File(directory, attachFileName);
                FileOutputStream outputStream = new FileOutputStream(storageDir);
                String extension = getfileExtension(attach_path);
                Log.d("***-**309",storageDir.getPath());

                absolute_path = storageDir.getPath();
                switch (extension){
                    case "jpg":
                        saveImage(inputStream, outputStream);
                        break;
                    case "wav":
                    case "mp4":
                    case "m4a":
                        saveVideoAudio(inputStream, outputStream);
                        break;
                }

                outputStream.close();
                dosyaEk.setText(attachFileName);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }


    private void saveImage(InputStream inputStream,OutputStream outputStream){

        Bitmap atch =  BitmapFactory.decodeStream(inputStream);
        try {
            atch.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            Toast.makeText(soruOlustur.this, "Attach Saved!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(soruOlustur.this, "Error while saving Attach!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }
    private void saveVideoAudio(InputStream inputStream, OutputStream outputStream) throws IOException {
        int bufferSize;
        byte[] bufffer = new byte[512];
        while ((bufferSize = inputStream.read(bufffer)) > 0) {
            outputStream.write(bufffer, 0, bufferSize);
        }
    }
    private String getfileExtension(Uri uri)
    {
        String extension;
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        extension= mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
        return extension;
    }



}