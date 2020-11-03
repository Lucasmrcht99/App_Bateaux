package com.example.app_bateaux;

import android.app.Activity;
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

public class AddSocieteActivity extends Activity {

    private EditText id;
    private EditText nom;
    private EditText mail;
    private EditText tel;
    private EditText adresse;
    public Intent suite;

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
        AddSocieteActivity addSocAct = this;

        ((Button) this.findViewById(R.id.buttonAjouterSoc)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String Id = id.getText().toString();
                String Nom = nom.getText().toString();
                String Mail = mail.getText().toString();
                String Tel = tel.getText().toString();
                String Adresse = adresse.getText().toString();

                DoAddSociete doAddSociete = new DoAddSociete(addSocAct, Id, Nom, Mail, Tel, Adresse);
                int code = doAddSociete.doInBackground();

                if(code == 201)
                {
                    BoatActivity.societe.setText(Id);
                    finish();
                }
                if(code == 401)
                {
                    AfficheToast.Affiche("Société existante !", addSocAct);
                }
            }
        });

        ((ImageButton) this.findViewById(R.id.imageButtonReturn4)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
