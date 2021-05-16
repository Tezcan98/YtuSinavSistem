package com.example.enestezcan;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Arrays;
import java.util.List;

public class SharedPrefSinifi {

    public static void setDefaultSettings(Context context, String tur, Integer zorluk, Integer sure) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPref.edit();
        edit.putString("tur", tur);
        edit.putInt("zorluk", zorluk);
        edit.putInt("sure", sure);
        edit.commit();
    }

    public static List<Object> getDefaultSettings(Context context) {
        String tur;
        Integer zorluk, sure;
        tur = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).getString("tur", "");
        zorluk = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).getInt("zorluk", 0);
        sure = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).getInt("sure", 0);
        return Arrays.asList(tur, zorluk, sure);
    }

}