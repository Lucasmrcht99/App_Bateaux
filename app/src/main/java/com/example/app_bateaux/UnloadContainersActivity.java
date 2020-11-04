package com.example.app_bateaux;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
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

public class UnloadContainersActivity extends Activity {

    private EditText containerId;
    private EditText societe;
    private EditText contenu;
    private EditText poids;
    private EditText dangers;
    private Spinner destination;
    private Button dechargerBtn;
    private ImageButton addSoc;
    public Intent suite;
    ObjectOutputStream oos=null;
    private Socket cliSock;
    private boolean stop = true;
    private boolean dechargement = true;
    private Bateau boat;
    private int capacite;
    private TextView boatInfo;
    private TextView boatCap;
    private Containers container;
    private int code;
    private String coord;
    private ArrayList<Containers> ListContainers;
    UnloadContainersActivity context;
    private static String Reponse="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unload_containers);
        Window window = getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(getColor(R.color.blue_app));

        UnloadContainersActivity context = this;
        containerId = (EditText) findViewById(R.id.editTextContainerId);
        societe = (EditText) findViewById(R.id.editTextSociete2);
        contenu = (EditText) findViewById(R.id.editTextContenu);
        poids = (EditText) findViewById(R.id.editTextPoids);
        dangers = (EditText) findViewById(R.id.editTextDangers);
        destination = (Spinner) findViewById(R.id.spinner1);
        dechargerBtn = (Button) findViewById(R.id.buttonAjouterContainer);
        addSoc = (ImageButton) findViewById(R.id.buttonAddSoc2);
        boatInfo = (TextView) findViewById(R.id.textViewBoatInfo3);
        boatCap =  (TextView) findViewById(R.id.textViewBoatInfo4);
        ListContainers = new ArrayList<Containers>();
        cliSock=SocketHandler.getSock();
        DoGetDestinations doGetDestinations = new DoGetDestinations(this);
        doGetDestinations.doInBackground();

        boat = MenuActivity.getBoat();
        boatInfo.setText("Bateau : " + boat.getId());
        boatCap.setText("Containers restants: " + boat.getCapacite());
        capacite = Integer.parseInt(boat.getCapacite());

        ((ImageButton) this.findViewById(R.id.imageButtonReturn5)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stop=false;
                finish();
            }
        });

        dechargerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dechargerBtn.getText().toString().equalsIgnoreCase("Décharger le container"))
                {
                    if(capacite > 0)
                    {
                        DoHandleContainerIn doHandleContainerIn = new DoHandleContainerIn(context, containerId.getText().toString(), societe.getText().toString(), poids.getText().toString());
                        code = doHandleContainerIn.doInBackground();
                        if(code == ReponseIOBREP.OK)
                        {
                            if(!coord.equals(""))
                            {
                                dechargement = false;
                                ListContainers.add(new Containers(containerId.getText().toString(), poids.getText().toString(), contenu.getText().toString(), societe.getText().toString(), dangers.getText().toString(), destination.getSelectedItem().toString(), coord));
                                coord="";
                                capacite--;
                                boatCap.setText("Containers restants: " + capacite);
                                if(capacite==0) {
                                    dechargerBtn.setText("Finir déchargement");
                                    containerId.setEnabled(false);
                                    societe.setEnabled(false);
                                    addSoc.setEnabled(false);
                                    poids.setEnabled(false);
                                    contenu.setEnabled(false);
                                    dangers.setEnabled(false);
                                    destination.setEnabled(false);
                                }
                                containerId.setText("");
                                societe.setText("");
                                contenu.setText("");
                                poids.setText("");
                                dangers.setText("");
                                AfficheToast.Affiche("Emplacement du container réservé !", context);
                            }
                            else
                            {
                                AfficheToast.Affiche("Erreur !", context);
                            }
                        }
                        else
                        {
                            if(code == ReponseIOBREP.FAIL)
                            {
                                AfficheToast.Affiche("Identifiant existant !", context);
                            }
                            if(code == ReponseIOBREP.FAIL2)
                            {
                                AfficheToast.Affiche("La société n'existe pas !", context);
                            }
                            if(code == ReponseIOBREP.FAIL3)
                            {
                                AfficheToast.Affiche("Le container est trop lourd !", context);
                            }
                            if(code == ReponseIOBREP.FAIL4)
                            {
                                AfficheToast.Affiche("Plus de place disponible", context);
                                if(ListContainers.size()==0)
                                {
                                    dechargerBtn.setText("Annuler déchargement");
                                }
                                else
                                {
                                    dechargerBtn.setText("Finir déchargement");
                                }
                            }
                        }
                    }
                }
                else if(dechargerBtn.getText().toString().equalsIgnoreCase("Finir déchargement"))
                {
                    dechargement = true;
                    DoFinishUnloading doFinishUnloading = new DoFinishUnloading(context, ListContainers, boat.getId());
                    code = doFinishUnloading.doInBackground();
                    if(code == ReponseIOBREP.OK) {
                        if(capacite > 0)
                        {
                            MenuActivity.setBoatNull();
                        }
                        else
                        {
                            MenuActivity.setBoatVide();
                        }
                        stop=false;
                        finish();
                        AfficheToast.Affiche(Reponse, MenuActivity.getContext());
                    }
                    else {
                        stop=false;
                        finish();
                        MenuActivity.setBoatNull();
                        AfficheToast.Affiche(Reponse, MenuActivity.getContext());
                    }
                }
                else if(dechargerBtn.getText().toString().equalsIgnoreCase("Annuler déchargement"))
                {
                    boat=null;
                    MenuActivity.setBoatNull();
                    stop=false;
                    finish();
                }
            }
        });

        addSoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suite = new Intent(context, AddSocieteActivity.class);
                context.startActivityForResult(suite, 1);
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
        destination.setAdapter(dataAdapter);
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

    public void setCoord(String c)
    {
        coord = c;
    }

    public void setReponse(String str)
    {
        Reponse = str;
    }

    @Override
    public void onBackPressed() {
        stop=false;
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dechargement == false)
        {
            DoFreeEmplacement doFreeEmplacement = new DoFreeEmplacement(context, ListContainers);
            doFreeEmplacement.doInBackground();
        }
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
