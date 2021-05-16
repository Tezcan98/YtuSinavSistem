package com.example.enestezcan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GirisEkrani extends AppCompatActivity {

    Button Loginbtn, goRegister;
    EditText email;
    EditText password;
    Integer hatali = 0;
    DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        Loginbtn = findViewById(R.id.login);
        goRegister = findViewById(R.id.goRegister);

        email = findViewById(R.id.emailinput);
        password = findViewById(R.id.passinput);

        Loginbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String emailInput = email.getText().toString();
                String PassInput = password.getText().toString();
                String msg;
                if(emailInput.trim().isEmpty() && PassInput.trim().isEmpty())
                    msg = "Lutfen bosluklari doldurun !";
                else {
                    Kullanici logined = db.girisYap(emailInput, PassInput);
                    if (logined != null) {
                        msg = "Giris Basarili";
                        Intent intent = new Intent(getApplicationContext(), indexEgitmen.class);
                        intent.putExtra("kullanici", (Serializable) logined);
                        startActivity(intent);
                    }
                    else{
                        msg = "Kullanici adi ve ya sifre hatali";
                        if (hatali==2){
                            Intent intent = new Intent(getApplicationContext(), KayitEkrani.class);
                            msg = "Sifre 3 Kere yanlış girildi. Üye olup tekrar girmeyi deneyin.";
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            startActivity(intent);
                        }
                        hatali++;

                    }
                }
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });

        goRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                Intent intent = new Intent(getApplicationContext(), KayitEkrani.class);
                startActivity(intent);
            }
        });

    }
}