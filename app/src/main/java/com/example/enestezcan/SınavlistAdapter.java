package com.example.enestezcan;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.List;

import static androidx.core.content.FileProvider.getUriForFile;

public class SınavlistAdapter extends RecyclerView.Adapter<SınavlistAdapter.SinavViewHolder>  {
    //this context we will use to inflate the layout
    private Context mCtx;
    private List<Sinav> sinavList;
    DatabaseHelper db;

    public SınavlistAdapter(Context mCtx, List<Sinav> sinavList) {
        this.mCtx = mCtx;
        this.sinavList = sinavList;
        db = new DatabaseHelper(mCtx);
    }
    @NonNull
    @Override
    public SinavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.olusturulan_sinavlar_listesi, null);
        return new SinavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SinavViewHolder holder, int position) {
        Sinav sinav = sinavList.get(position);
        holder.sinavIsim.setText(sinav.getSinavisim());
        holder.tarihOlusturma.setText(sinav.getOlusturmaTarihi());

        if(sinav.getBasladi())
            holder.tarihBaslama.setText(sinav.getAktifTarihi());
        else
            holder.tarihBaslama.setText("Henüz Başlamadi");

        holder.baslat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Here You Do Your Click Magic
                if(sinav.getBasladi()) {
                    // puani hesaplama fonksiyonu
                    holder.baslat.setText("Tekrar Başlat");

                }else{
                    sinav.baslat();
                    db.sinavBaslat(sinav.getSinavID());
                    Toast.makeText(mCtx, "Sinav Başlatıldı", Toast.LENGTH_SHORT).show();
                    holder.tarihBaslama.setText(sinav.getAktifTarihi());
                    holder.baslat.setText("Bitir ve puan hesapla");

                    List<Soru> sinavList = db.sinavinSorulari(sinav.getSinavID());
                    String Gonderilen = sinav.sinavYazisi();

                    for (Soru soru: sinavList){
                        soru.setSinav(sinav); // sinavın zorluğuna göre şık sayısı belirleniyor
                        Gonderilen += soru.getSoruMetni() + "\n" + soru.getSiklar();
                    }

                    writeToFile(Gonderilen);

                }
            }
        });

        holder.duzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Here You Do Your Click Magic
                Intent intent = new Intent(mCtx, SinavOlustur.class);
                intent.putExtra("kullanici" , (Serializable) sinav.getOlusturan());
                intent.putExtra("sinav" , sinav);
                mCtx.startActivity(intent);
            }
        });
    }
    private void writeToFile(String data) {
        try {
            ContextWrapper cw = new ContextWrapper(mCtx);
            File directory = cw.getCacheDir();
            File storageDir = new File(directory, "sinav.txt");
            FileOutputStream outputStream = new FileOutputStream(storageDir);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.write(data);
            outputStreamWriter.close();

            Intent share = new Intent();
            share.setAction(Intent.ACTION_SEND);
            share.setType("text/*");
            Uri uri = getUriForFile(mCtx, "com.enestezcan.fileprovider", storageDir);
            share.putExtra(Intent.EXTRA_STREAM,  uri);
            mCtx.startActivity(Intent.createChooser(share, "share file with"));

        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    @Override
    public int getItemCount() {
        return sinavList.size();
    }

    public class SinavViewHolder extends RecyclerView.ViewHolder {
        TextView sinavIsim, tarihOlusturma, tarihBaslama;
        Button baslat, duzenle;
        public SinavViewHolder(@NonNull View itemView) {
            super(itemView);

            sinavIsim = itemView.findViewById(R.id.soru);
            tarihOlusturma = itemView.findViewById(R.id.t1);
            tarihBaslama = itemView.findViewById(R.id.t2);
            baslat = itemView.findViewById(R.id.sonucHesapla);
            duzenle = itemView.findViewById(R.id.duzenle);


        }
    }

}

