package com.example.app_bateaux;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        String datearriv = formatter.format(curdate);

        String insertmouv = "insert into mouvements (container_id,transport_entrant,date_arriv,poids,destination) values('"
                          + container + "','" + trE +"','" + datearriv + "','" + poids + "','" + destination +"')";

        this.getWritableDatabase().execSQL(insertmouv);
        Log.i("DATABASE","insertMouv invoked : " + container );
    }

    public void insertbidouillage(String container, String trE, String poids, String destination,String d)
    {

        String insertmouv = "insert into mouvements (container_id,transport_entrant,date_arriv,poids,destination) values('"
                + container + "','" + trE +"','" + d + "','" + poids + "','" + destination +"')";

        this.getWritableDatabase().execSQL(insertmouv);
        Log.i("DATABASE","insertMouv invoked : " + container );
    }

    public void insertbidouillage2(String container, String trS,String Dest,String d)
    {

        String insertmouv = "insert into mouvements (container_id,transport_sortant,date_depart,destination) values('"
                + container + "','" + trS +"','" + d + "','" + Dest +"')";
        this.getWritableDatabase().execSQL(insertmouv);
        Log.i("DATABASE","insertMouv invoked : " + container );
    }

    public void insertorupdatemouvdepart(String container, String trS,String Dest)
    {
        Date curdate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
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

    public  ArrayList<String> recupDates(boolean mode)
    {
        ArrayList<String> dates = new ArrayList<>();
        boolean find = false;

        if(mode == false)
        {
            String req = "Select date_arriv from mouvements where date_arriv is not null order by date_arriv ASC";
            Cursor cursor = this.getReadableDatabase().rawQuery(req,null);
            cursor.moveToFirst();

            while(!cursor.isAfterLast())
            {
                find= true;
                dates.add(cursor.getString(0));
                cursor.moveToNext();
            }
        }
        else
        {
            String req = "Select date_depart from mouvements where date_depart is not null order by date_depart ASC";
            Cursor cursor = this.getReadableDatabase().rawQuery(req,null);
            cursor.moveToFirst();

            while(!cursor.isAfterLast())
            {
                find= true;
                dates.add(cursor.getString(0));
                cursor.moveToNext();
            }
        }

        if(find==false)
        {
            dates.add(" ");
        }
        Log.i("DATABASE","RecupDate invoked mode : " + mode );

        return dates;
    }

    public  ArrayList<String> recupDatespie(boolean mode,String date)
    {
        ArrayList<String> dest = new ArrayList<>();
        boolean find = false;
        StringTokenizer st = new StringTokenizer(date,"/");
        String annee = st.nextToken();
        String mois = st.nextToken();
        int jour = Integer.parseInt(st.nextToken())+6;
        String chaine = annee+"/"+mois+"/"+jour;



        if(mode == false)
        {
            String req = "Select destination from mouvements where date_arriv is not null AND date_arriv>='"+date+"' AND date_arriv<='"+chaine+"'  order by destination ";
            Cursor cursor = this.getReadableDatabase().rawQuery(req,null);
            cursor.moveToFirst();

            while(!cursor.isAfterLast())
            {
                find= true;
                dest.add(cursor.getString(0));
                cursor.moveToNext();
            }
        }
        else
        {
            String req = "Select destination from mouvements where date_depart is not null AND date_depart>='"+date+"' AND date_depart<='"+chaine+"' order by destination ";
            Cursor cursor = this.getReadableDatabase().rawQuery(req,null);
            cursor.moveToFirst();

            while(!cursor.isAfterLast())
            {
                find= true;
                dest.add(cursor.getString(0));
                cursor.moveToNext();
            }
        }

        if(find==false)
        {
            dest.add(" ");
        }
        Log.i("DATABASE","RecupDest invoked mode : " + mode );

        return dest;
    }

}
