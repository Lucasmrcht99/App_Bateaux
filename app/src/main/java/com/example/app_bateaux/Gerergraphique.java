package com.example.app_bateaux;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import req_rep_IOBREP.RequeteIOBREP;

public class Gerergraphique extends Activity {

    ObjectOutputStream oos=null;
    private Socket cliSock;
    private boolean stop = true;
    private ImageButton btnReturn;
    private Button btnHisto;
    private Button btnRepre;
    private Context context = this;
    private DatabaseManager db;
    private Button bidonnage;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphique);

        Window window = getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(getColor(R.color.blue_app));
        cliSock=SocketHandler.getSock();
        btnReturn = (ImageButton) findViewById(R.id.imageButtonReturn6);
        btnHisto = (Button) findViewById((R.id.buttonHistogramme));
        btnRepre = (Button) findViewById((R.id.buttonRepresentation));
        bidonnage = findViewById(R.id.buttonBidonnage);

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop=false;
                finish();
            }
        });

        btnHisto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent suite = new Intent(context, AffHistogramme.class);
                context.startActivity(suite);
            }
        });

        btnRepre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent suite = new Intent(context, AffPie.class);
                context.startActivity(suite);

            }
        });

        bidonnage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = new DatabaseManager(context);

                db.insertbidouillage("C005","B007","15","Anvers","2020/11/16");
                db.insertbidouillage("C006","B007","15","Hasselt","2020/11/16");
                db.insertbidouillage("C007","B007","15","Anvers","2020/11/15");
                db.insertbidouillage("C008","B007","15","Anvers","2020/11/12");
                db.insertbidouillage("C009","B007","15","Hasselt","2020/11/12");
                db.insertbidouillage("C010","B007","15","Hasselt","2020/11/11");
                db.insertbidouillage("C011","B007","15","Hasselt","2020/11/11");
                db.insertbidouillage("C012","B007","15","Liege","2020/11/11");
                db.insertbidouillage("C013","B007","15","Liege","2020/11/10");
                db.insertbidouillage("C014","B007","15","Liege","2020/11/10");
                db.insertbidouillage("C015","B007","15","Seraing","2020/11/10");
                db.insertbidouillage("C016","B007","15","Liege","2020/11/09");
                db.insertbidouillage("C017","B007","15","Seraing","2020/11/09");
                db.insertbidouillage("C018","B007","15","Seraing","2020/11/09");
                db.insertbidouillage("C019","B007","15","Seraing","2020/11/09");
                db.insertbidouillage("C020","B007","15","Seraing","2020/11/08");
                db.insertbidouillage("C021","B007","15","Huy","2020/11/08");
                db.insertbidouillage("C022","B007","15","Huy","2020/11/08");
                db.insertbidouillage("C023","B007","15","Huy","2020/11/07");
                db.insertbidouillage("C024","B007","15","Dinant","2020/11/07");
                db.insertbidouillage("C025","B007","15","Seraing","2020/11/06");
                db.insertbidouillage("C026","B007","15","Maastricht","2020/11/06");
                db.insertbidouillage("C027","B007","15","Dinant","2020/11/06");
                db.insertbidouillage("C028","B007","15","Maastricht","2020/11/06");


                db.insertbidouillage2("C005","B007","Liege","2020/11/16");
                db.insertbidouillage2("C006","B007","Liege","2020/11/16");
                db.insertbidouillage2("C007","B007","Anvers","2020/11/15");
                db.insertbidouillage2("C008","B007","Anvers","2020/11/12");
                db.insertbidouillage2("C009","B007","Seraing","2020/11/12");


                db.close();
            }
        });

    }


    @Override
    public void onBackPressed() {
        stop=false;
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(stop==true) {
            try {
                RequeteIOBREP req2 = new RequeteIOBREP(RequeteIOBREP.CLOSE, "");
                oos = new ObjectOutputStream(cliSock.getOutputStream());
                oos.writeObject(req2);
                oos.flush();
            } catch (IOException e) {
                System.out.println("Connexion au serveur perdue");
            }
        }

    }
}
