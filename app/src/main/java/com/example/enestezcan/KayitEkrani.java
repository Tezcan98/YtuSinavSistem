package com.example.enestezcan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;

public class KayitEkrani extends AppCompatActivity {

    Button BackToLogin, Register, LoadImage;
    EditText name,email,password,password2;
    ImageView resimOnizle;
    public static final int PICK_IMAGE = 1;
    private DatabaseHelper db;
    private static int GALLERY_REQUEST = 100;
    private String image_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        db = new DatabaseHelper(this);
        
//        Intent intent = getIntent();
//        List<Kullanici> Kullanıcılar = (List<Kullanici> )intent.getSerializableExtra("KullaniciListesi");
        BackToLogin = findViewById(R.id.returnLogin);
        Register = findViewById(R.id.register);
        LoadImage = findViewById(R.id.resimsec);
        resimOnizle = findViewById(R.id.onizleme);
        email = findViewById(R.id.r_emailinput);
        name = findViewById(R.id.nameinput);
        password = findViewById(R.id.r_passinput);
        password2 = findViewById(R.id.r_passinput2);

        BackToLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), GirisEkrani.class);
//                intent.putExtra("KullaniciListesi" , (Serializable)Kullanıcılar);
                startActivity(intent);
            }
        });
        LoadImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST);
            }
        });
        Register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String NameInput = name.getText().toString();
                String emailInput = email.getText().toString();
                String passwordInput = password.getText().toString();
                String passwordInput2 = password2.getText().toString();

                if(NameInput.trim().isEmpty() && emailInput.trim().isEmpty() && passwordInput.trim().isEmpty() && passwordInput2.trim().isEmpty() ){
                    Toast.makeText(getApplicationContext(), "Lutfen boslukları doldurun ! ", Toast.LENGTH_LONG).show();

                }
                else
                    if(!passwordInput.equals(passwordInput2))
                        Toast.makeText(getApplicationContext(), "Sifreler Aynı degil ! ", Toast.LENGTH_LONG).show();
                    else {
                        if(emailInput.indexOf("@") == -1 || emailInput.indexOf(".") == -1){
                            Toast.makeText(getApplicationContext(), "Lütfen Geçerli bir Email adresi giriniz ", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Kullanici newKullanici = new Kullanici(NameInput, emailInput, passwordInput, image_path);
                            if(db.kullaniciOlustur(newKullanici, true) != -1) {
                                Toast.makeText(getApplicationContext(), "Kullanıcı Basarı ile oluşturuldu, Giriş ekranına dönebilirsiniz..", Toast.LENGTH_LONG).show();
                                name.setText("");
                                email.setText("");
                                password.setText("");
                                password2.setText("");
                            }
                            else
                                Toast.makeText(getApplicationContext(), "Bu Emaile sahip bir kullanici bulunmaktadir.", Toast.LENGTH_LONG).show();
                        }
                    }



            }
        });
    }

    private void saveImage(Bitmap image, String imageFileName) {

        String dirPath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/"+getString(R.string.app_name)   ;
        final File storageDir = new File(dirPath);

        boolean successDirCreated = true;
        if (!storageDir.exists()) {
            successDirCreated = storageDir.mkdir();

        }
        if (successDirCreated) {
            File imageFile = new File(storageDir, imageFileName);
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
                image_path = imageFile.getAbsolutePath();
                Toast.makeText(KayitEkrani.this, "Image Saved!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(KayitEkrani.this, "Error while saving image!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }else{
            Toast.makeText(this, "Failed to make folder!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && null != data) {
            try {
                Uri selectedImage = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                resimOnizle.setImageBitmap(BitmapFactory.decodeStream(imageStream));

                Bitmap image = ((BitmapDrawable)resimOnizle.getDrawable()).getBitmap();
                saveImage(image, selectedImage.getPath().substring(selectedImage.getPath().lastIndexOf( '/')+1));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}