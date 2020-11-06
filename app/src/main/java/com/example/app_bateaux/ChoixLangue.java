package com.example.app_bateaux;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import req_rep_IOBREP.RequeteIOBREP;

public class ChoixLangue extends Activity {

    private ImageView fr;
    private ImageView en;
    private ImageView de;
    private Context context = this;
    private String user;
    private String fonction;
    ObjectOutputStream oos=null;
    private Socket cliSock;
    private boolean stop = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choixlangue);
        Window window = getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(getColor(R.color.blue_app));


        user = (String)this.getIntent().getExtras().get("User");
        fonction = (String)this.getIntent().getExtras().get("Fonction");
        fr = (ImageView) findViewById(R.id.imageViewFr);
        en = (ImageView) findViewById(R.id.imageViewEn);
        de = (ImageView) findViewById(R.id.imageViewDe);

        cliSock = SocketHandler.getSock();

        fr.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(context, MenuActivity.class);
                intent.putExtra("User", user);
                intent.putExtra("Fonction", fonction);
                intent.putExtra("Langue", "fr");

                startActivity(intent);
                stop=false;
                finish();

            }
        });

        en.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(context, MenuActivity.class);
                intent.putExtra("User", user);
                intent.putExtra("Fonction", fonction);
                intent.putExtra("Langue", "en");

                startActivity(intent);
                stop=false;
                finish();

            }
        });

        de.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(context, MenuActivity.class);
                intent.putExtra("User", user);
                intent.putExtra("Fonction", fonction);
                intent.putExtra("Langue", "de");
                startActivity(intent);
                stop=false;
                finish();

            }
        });


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
