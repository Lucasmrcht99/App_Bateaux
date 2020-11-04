package com.example.app_bateaux;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import req_rep_IOBREP.ReponseIOBREP;
import req_rep_IOBREP.RequeteIOBREP;

public class BoatActivity extends Activity {
    private EditText id;
    public static EditText societe;
    private EditText cap;
    private Spinner dest;
    private CheckBox vide;
    public Intent suite;
    ObjectOutputStream oos=null;
    private Socket cliSock;
    private boolean stop = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boat_arrived);
        Window window = getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(getColor(R.color.blue_app));

        id = (EditText) this.findViewById(R.id.editTextId);
        societe = (EditText) this.findViewById(R.id.editTextSociete);
        cap = (EditText) this.findViewById(R.id.editTextCapacite);
        dest = (Spinner) this.findViewById(R.id.spinner2);
        vide = (CheckBox) this.findViewById(R.id.checkBoxVide);
        BoatActivity boatActivity = this;
        cliSock=SocketHandler.getSock();
        DoGetDestinations doGetDest = new DoGetDestinations(this);
        doGetDest.doInBackground();

        ((ImageButton) this.findViewById(R.id.imageButtonReturn2)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stop=false;
                finish();
            }
        });

        ((ImageButton) this.findViewById(R.id.buttonAddSoc)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suite = new Intent(boatActivity, AddSocieteActivity.class);
                boatActivity.startActivityForResult(suite, 1);
            }
        });

        ((Button) this.findViewById(R.id.buttonValider)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String Id = id.getText().toString();
                String Societe = societe.getText().toString();
                String Capacite = cap.getText().toString();
                String Dest = dest.getSelectedItem().toString();
                String Vide = "plein";
                if (vide.isChecked())
                {
                    Vide = "vide";
                }

                DoAddBoat doAddBoat = new DoAddBoat(boatActivity, Id, Societe, Capacite);
                int code = doAddBoat.doInBackground();

                if(code == ReponseIOBREP.OK || code == ReponseIOBREP.FAIL)
                {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("boatId", Id);
                    returnIntent.putExtra("capacite", Capacite);
                    returnIntent.putExtra("destination", Dest);
                    returnIntent.putExtra("vide", Vide);
                    setResult(1,returnIntent);
                    stop=false;
                    finish();
                }
                if(code == ReponseIOBREP.FAIL2)
                {
                    AfficheToast.Affiche("Société inexistante !", boatActivity);
                }
            }
        });

    }

    public void addItemsOnSpinner(String str) {
        List<String> list = new ArrayList<String>();

        StringTokenizer st = new StringTokenizer(str, "&");
        while(st.hasMoreTokens())
            list.add(st.nextToken());

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        dest.setAdapter(dataAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == 1){
                String soc =data.getStringExtra("societe");
                societe.setText(soc);
            }
        }
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
