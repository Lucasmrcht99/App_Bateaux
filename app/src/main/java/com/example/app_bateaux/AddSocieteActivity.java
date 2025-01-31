package com.example.app_bateaux;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import req_rep_IOBREP.ReponseIOBREP;
import req_rep_IOBREP.RequeteIOBREP;

public class AddSocieteActivity extends Activity {

    private EditText id;
    private EditText nom;
    private EditText mail;
    private EditText tel;
    private EditText adresse;
    public Intent suite;
    ObjectOutputStream oos=null;
    private boolean stop = true;
    private Socket cliSock;
    private Context context = this;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_societe);
        Window window = getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(getColor(R.color.blue_app));

        id = (EditText) this.findViewById(R.id.editTextIdSoc);
        nom = (EditText) this.findViewById(R.id.editTextNom);
        mail = (EditText) this.findViewById(R.id.editTextMail);
        tel = (EditText) this.findViewById(R.id.editTextTel);
        adresse = (EditText) this.findViewById(R.id.editTextAdresse);
        cliSock=SocketHandler.getSock();
        AddSocieteActivity addSocAct = this;

        ((Button) this.findViewById(R.id.buttonAjouterSoc)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String Id = id.getText().toString();
                String Nom = nom.getText().toString();
                String Mail = mail.getText().toString();
                String Tel = tel.getText().toString();
                String Adresse = adresse.getText().toString();

                if(Id.equals("") || Nom.equals("") || Mail.equals("") || Tel.equals("") || Adresse.equals(""))
                {
                    AfficheToast.Affiche("Champ vide !",context);
                }
                else
                {
                    DoAddSociete doAddSociete = new DoAddSociete(addSocAct, Id, Nom, Mail, Tel, Adresse);
                    int code = doAddSociete.doInBackground();

                    if(code == ReponseIOBREP.OK)
                    {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("societe", Id);
                        setResult(1,returnIntent);
                        stop=false;
                        finish();
                    }
                    if(code == ReponseIOBREP.FAIL)
                    {
                        AfficheToast.Affiche("Société existante !", addSocAct);
                    }
                }
            }
        });

        ((ImageButton) this.findViewById(R.id.imageButtonReturn4)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop=false;
                finish();
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
