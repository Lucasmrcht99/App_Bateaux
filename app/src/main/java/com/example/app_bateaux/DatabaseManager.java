package com.example.app_bateaux;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME ="Operations.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseManager(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tablemouv = "create table mouvements ("
                         + "    id integer primary key autoincrement,"
                         + "    container_id text,"
                         + "    transport_entrant text,"
                         + "    date_arriv date,"
                         + "    transport_sortant text,"
                         + "    poids integer ,"
                         + "    date_depart date,"
                         + "    destination text"
                         + ")";

        db.execSQL(tablemouv);
        Log.i("DATABASE","onCreate invoked");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String tablemouv ="drop table mouvements";
        db.execSQL(tablemouv);
        this.onCreate(db);
        Log.i("DATABASE","onUpgrade invoked");
    }

    public void insertmouvarriv(String container, String trE, String poids, String destination)
    {
        Date curdate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("YYYY/MM/DD");
        String datearriv = formatter.format(curdate);

        String insertmouv = "insert into mouvements (container_id,transport_entrant,date_arriv,poids,destination) values('"
                          + container + "','" + trE +"','" + datearriv + "','" + poids + "','" + destination +"')";

        this.getWritableDatabase().execSQL(insertmouv);
        Log.i("DATABASE","insertMouv invoked : " + container );
    }

    public void insertorupdatemouvdepart(String container, String trS,String Dest)
    {
        Date curdate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("YYYY/MM/DD");
        String datedepart= formatter.format(curdate);
        boolean find = false;

        String verif = "Select container_id from mouvements where container_id='"+container+"'";
        Cursor cursor = this.getReadableDatabase().rawQuery(verif,null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast())
        {
            String updatemouv ="update mouvements SET transport_sortant='"+trS+"',date_depart='"+datedepart+"' WHERE container_id='"+container+"'";
            this.getWritableDatabase().execSQL(updatemouv);
            Log.i("DATABASE","UpdateMouv invoked : " + container );
            find=true;
            cursor.moveToNext();
        }
        if( find==false)
        {
            String insertmouv = "insert into mouvements (container_id,transport_sortant,date_depart,destination) values('"
                    + container + "','" + trS +"','" + datedepart + "','" + Dest +"')";
            this.getWritableDatabase().execSQL(insertmouv);
            Log.i("DATABASE","insertMouv invoked : " + container );
        }
    }

}
