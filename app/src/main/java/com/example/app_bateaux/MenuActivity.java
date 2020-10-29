package com.example.app_bateaux;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        user = (String)this.getIntent().getExtras().get("User");
        cliSock = SocketHandler.getSock();

        TextView textView = (TextView)findViewById(R.id.textViewUser);
        textView.setText(user);
        MenuActivity menuAct = this;

        Button bBoat = (Button)this.findViewById(R.id.buttonBoat);
        bBoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button bGetCont = (Button)this.findViewById(R.id.buttonGetContainers);
        bGetCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suite = new Intent(menuAct, LoadContainersActivity.class);
                suite.putExtra("User", user);
                menuAct.startActivity(suite);
            }
        });

        Button bListCont = (Button)this.findViewById(R.id.buttonAffCont);
        bListCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suite = new Intent(menuAct, ViewContainersAcitivity.class);
                suite.putExtra("User", user);
                menuAct.startActivity(suite);
            }
        });
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
