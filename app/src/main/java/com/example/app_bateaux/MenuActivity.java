package com.example.app_bateaux;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import req_rep_IOBREP.RequeteIOBREP;

public class MenuActivity extends Activity {

    private String user="";
    ObjectInputStream ois=null;
    ObjectOutputStream oos=null;
    private Socket cliSock;
    public Intent suite;
    private  String lang="";
    private Bateau boat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        Window window = getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(getColor(R.color.blue_app));
        user = (String)this.getIntent().getExtras().get("User");
        lang = (String)this.getIntent().getExtras().get("Langue");
        boat = new Bateau();

        cliSock = SocketHandler.getSock();

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

        Button bLoadCont = (Button)this.findViewById(R.id.buttonGetContainers);
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
                suite = new Intent(menuAct, ViewContainersAcitivity.class);
                menuAct.startActivity(suite);
            }
        });

        if(lang.equalsIgnoreCase("1"))
        {
            bBoat.setText("Boat Arrived");
            bListCont.setText("List of containers");
            bLoadCont.setText("Loading of containers");
        }
        else if (lang.equalsIgnoreCase("2"))
        {
            TextView texttop = (TextView)findViewById(R.id.textView);
            texttop.setText("Menü");
            bBoat.setText("Boot angekommen");
            bListCont.setText("Liste der Container");
            bLoadCont.setText("Verladen von Container");
        }
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

                Bateau b = new Bateau(id,cap,dest,vide);
                TextView idBoat = (TextView) findViewById(R.id.textViewBateau);
                idBoat.setText(b.getId());
            }
        }
    }

    public void setBoatNull()
    {
        boat = new Bateau();
    }

    public Bateau getBoat()
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
}
