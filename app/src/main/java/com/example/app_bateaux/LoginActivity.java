package com.example.app_bateaux;

import android.app.Activity;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

    private String login="";
    private String pwd="";
    public Intent suite;
    private Button bLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bLogin = (Button)this.findViewById(R.id.buttonLogin);
        LoginActivity logAct = this;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edit = (EditText)findViewById(R.id.editTextLogin);
                login = edit.getText().toString();
                edit = (EditText)findViewById(R.id.editTextPassword);
                pwd = edit.getText().toString();
                System.out.println(login + pwd);
                if(!login.equals("") && login!=null && !pwd.equals("") && pwd!=null)
                {
                    DoLogin doLog = new DoLogin(login, pwd, logAct);
                    doLog.doInBackground();
                }
            }
        });
    }

    public void AfficheToast(String msg)
    {
        Toast.makeText(this,msg, Toast.LENGTH_LONG).show();
    }
}