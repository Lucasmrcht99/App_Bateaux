package com.example.app_bateaux;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Locale;

import req_rep_IOBREP.RequeteIOBREP;

public class MenuActivity extends Activity {

    private String user="";
    ObjectInputStream ois=null;
    ObjectOutputStream oos=null;
    private Socket cliSock;
    public Intent suite;
    private  String lang="";
    private static Bateau boat;
    private static TextView idBoat;
    private static Context context;
    private Resources resources;
    private Locale Langlocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lang = (String)this.getIntent().getExtras().get("Langue");
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
        getBaseContext().getResources().getDisplayMetrics());
        this.setContentView(R.layout.menu);

        Window window = getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(getColor(R.color.blue_app));
        user = (String)this.getIntent().getExtras().get("User");
        boat = new Bateau();
        idBoat = (TextView) findViewById(R.id.textViewBateau);

        cliSock = SocketHandler.getSock();
        context=this;
        TextView textView = (TextView)findViewById(R.id.textViewUser);
        textView.setText(user);
        MenuActivity menuAct = this;

        Button bBoat = (Button)this.findViewById(R.id.buttonBoat);
        bBoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(boat.getId().equals(""))
                {
                    suite = new Intent(menuAct, BoatActivity.class);
                    menuAct.startActivityForResult(suite, 1);
                }
                else
                {
                    AfficheToast.Affiche("Il y a déjà un bateau à quai !", menuAct);
                }
            }
        });

        Button bLoadCont = (Button)this.findViewById(R.id.buttonLoadContainers);
        bLoadCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!boat.getId().equals(""))
                {
                    if(boat.getVide().equals("vide"))
                    {
                        suite = new Intent(menuAct, LoadContainersActivity.class);
                        menuAct.startActivity(suite);
                    }
                    else
                    {
                        AfficheToast.Affiche("Le bateau n'est pas vide !", menuAct);
                    }
                }
                else
                {
                    AfficheToast.Affiche("Il n'y a pas de bateau à quai !", menuAct);
                }
            }
        });

        Button bListCont = (Button)this.findViewById(R.id.buttonAffCont);
        bListCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suite = new Intent(menuAct, ViewContainersActivity.class);
                menuAct.startActivity(suite);
            }
        });

        Button graph = (Button)this.findViewById(R.id.buttonGraphique);
        graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suite = new Intent(menuAct, Gerergraphique.class);
                menuAct.startActivity(suite);
            }
        });

        Button unLoadCont = (Button)this.findViewById(R.id.buttonUnloadContainers);
        unLoadCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!boat.getId().equals(""))
                {
                    if(!boat.getVide().equals("vide"))
                    {
                        suite = new Intent(menuAct, UnloadContainersActivity.class);
                        menuAct.startActivity(suite);
                    }
                    else
                    {
                        AfficheToast.Affiche("Le bateau est déjà vide !", menuAct);
                    }
                }
                else
                {
                    AfficheToast.Affiche("Il n'y a pas de bateau à quai !", menuAct);
                }
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == 1){
                String id =data.getStringExtra("boatId");
                String cap =data.getStringExtra("capacite");
                String dest =data.getStringExtra("destination");
                String vide =data.getStringExtra("vide");

                boat = new Bateau(id,cap,dest,vide);
                idBoat.setText(boat.getId());
            }
        }
    }

    public static void setBoatNull()
    {
        boat = new Bateau();
        idBoat.setText("");
    }

    public static void setBoatVide()
    {
        boat.setVide("vide");
    }

    public static Bateau getBoat()
    {
        return boat;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            RequeteIOBREP req2 = new RequeteIOBREP(RequeteIOBREP.CLOSE, "");
            oos = new ObjectOutputStream(cliSock.getOutputStream());
            oos.writeObject(req2);
            oos.flush();
        }
        catch (IOException e) {
            System.out.println("Connexion au serveur perdue");
        }
    }

    public static Context getContext() {
        return context;
    }
}
