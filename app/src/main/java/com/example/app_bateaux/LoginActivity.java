package com.example.app_bateaux;

import android.app.Activity;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class LoginActivity extends Activity {

    private String login="";
    private String pwd="";
    public Intent suite;
    private Button bLogin;
    private Spinner spinner;
    private String langue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bLogin = (Button)this.findViewById(R.id.buttonLogin);
        LoginActivity logAct = this;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        spinner = (Spinner) findViewById(R.id.spinner);

        setlangue();
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edit = (EditText)findViewById(R.id.editTextLogin);
                login = edit.getText().toString();
                edit = (EditText)findViewById(R.id.editTextPassword);
                pwd = edit.getText().toString();
                langue = spinner.getSelectedItem().toString();
                System.out.println(login + pwd);
                if(!login.equals("") && login!=null && !pwd.equals("") && pwd!=null)
                {
                    DoLogin doLog = new DoLogin(login, pwd, logAct,langue);
                    doLog.doInBackground();
                }
            }
        });
    }

    public void setlangue() {

        ArrayList<String> Listlangue = new ArrayList<String>();
        Listlangue.add("Francais");
        Listlangue.add("English");
        Listlangue.add("German");

        spinner.removeAllViewsInLayout();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Listlangue);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(dataAdapter);
    }
}