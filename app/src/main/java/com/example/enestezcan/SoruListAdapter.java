package com.example.enestezcan;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import static androidx.core.content.FileProvider.getUriForFile;

public class SoruListAdapter extends RecyclerView.Adapter<SoruListAdapter.SinavViewHolder>  {
    private Context mCtx;
    private List<Soru> soruList;
    DatabaseHelper db;

    public SoruListAdapter(Context mCtx, List<Soru> soruList) {
        this.mCtx = mCtx;
        this.soruList = soruList;
        db = new DatabaseHelper(mCtx);
    }
    @NonNull
    @Override
    public SinavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.olusturulan_sorular_listesi, null);
        return new SinavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SinavViewHolder holder, int position) {
        Soru soru = soruList.get(position);
        holder.dersimi.setText(soru.getSinavIsmi());
        holder.zorluk.setText("Zorluk Seviyesi : "+soru.getSınavZorluk());
        holder.soru.setText(soru.getSoruMetni());
        char dogruSikAscii = (char)('A'+soru.getDogruSik());
        holder.dogruSik.setText("Dogru Sik : " + dogruSikAscii);
        holder.siklar.setText(soru.getSiklar());
        if(soru.getEk().equals(""))
            holder.soruek.setVisibility(View.INVISIBLE);
        else{
            String image_path = soru.getEk();
//            File storageDir = new File(image_path);
//            Uri uri = getUriForFile(mCtx, "com.enestezcan.fileprovider", storageDir);
//            holder.soruek.setImageURI(uri);
        }


        holder.duzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, soruOlustur.class);
                intent.putExtra("kullanici" , soru.getSinav().getOlusturan());
                intent.putExtra("soru" , soru);
                mCtx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return soruList.size();
    }

    public class SinavViewHolder extends RecyclerView.ViewHolder {
        TextView zorluk, dersimi, soru, siklar, dogruSik;
        Button duzenle;
        ImageView soruek;
        public SinavViewHolder(@NonNull View itemView) {
            super(itemView);

            zorluk = itemView.findViewById(R.id.zorluk);
            dersimi = itemView.findViewById(R.id.sorunundersi);
            soru = itemView.findViewById(R.id.soru);
            siklar = itemView.findViewById(R.id.siklar);
            dogruSik = itemView.findViewById(R.id.dogru_sik);
            duzenle = itemView.findViewById(R.id.duzenle);
            soruek = itemView.findViewById(R.id.sorueki);


        }
    }
    public Bitmap getImage(String image_path) {
        try {
            InputStream fileIn = new FileInputStream(image_path);
            Bitmap image = BitmapFactory.decodeStream(fileIn);
            fileIn.close();
            return image;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

