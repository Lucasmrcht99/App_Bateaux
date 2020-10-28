package com.example.app_bateaux;

import android.app.Activity;
import android.os.Bundle;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import req_rep_IOBREP.RequeteIOBREP;

public class LoadContainers extends Activity {

    ObjectInputStream ois=null;
    ObjectOutputStream oos=null;
    private Socket cliSock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_containers);
        cliSock=SocketHandler.getSock();
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
