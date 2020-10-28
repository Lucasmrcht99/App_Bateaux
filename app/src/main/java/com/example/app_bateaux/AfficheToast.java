package com.example.app_bateaux;

import android.content.Context;
import android.widget.Toast;

public class AfficheToast {
    public static void Affiche(String msg, Context context) {
        Toast.makeText(context,msg, Toast.LENGTH_LONG).show();
    }
}
