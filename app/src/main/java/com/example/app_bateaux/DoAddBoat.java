package com.example.app_bateaux;

import android.content.Intent;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import req_rep_IOBREP.ReponseIOBREP;
import req_rep_IOBREP.RequeteIOBREP;

public class DoAddBoat extends AsyncTask<Void, Void, Integer> {

    private BoatActivity boatAct;
    private String Id;
    private String Societe;
    private String Capacite;

    public DoAddBoat(BoatActivity context, String id, String soc, String cap)
    {
        boatAct = context;
        Id = id;
        Societe = soc;
        Capacite = cap;
    }

    @Override protected void onPreExecute() { super.onPreExecute(); }

    @Override protected void onProgressUpdate(Void... arg1)
    {
        super.onProgressUpdate();
    }

    @Override
    protected Integer doInBackground(Void... arg0) {
        ObjectInputStream ois=null;
        ObjectOutputStream oos=null;
        RequeteIOBREP req = new RequeteIOBREP(RequeteIOBREP.ADD_BOAT, Id + "&" + Societe + "&" + Capacite);
        Socket cliSock = SocketHandler.getSock();
        try {
            oos = new ObjectOutputStream(cliSock.getOutputStream());
            oos.writeObject(req);
            oos.flush();
        }
        catch (IOException e)
        {
            System.out.println("--- erreur IO = " + e.getMessage());
            AfficheToast.Affiche( "Connexion au serveur perdue", boatAct);
            boatAct.suite = new Intent(boatAct, LoginActivity.class);
            boatAct.startActivity(boatAct.suite);
            boatAct.finish();
        }
        // Lecture de la réponse
        ReponseIOBREP rep = null;
        try
        {
            ois = new ObjectInputStream(cliSock.getInputStream());
            rep = (ReponseIOBREP)ois.readObject();
        }
        catch (IOException e)
        {
            System.out.println("--- erreur IO = " + e.getMessage());
            AfficheToast.Affiche( "Connexion au serveur perdue", boatAct);
            boatAct.suite = new Intent(boatAct, LoginActivity.class);
            boatAct.startActivity(boatAct.suite);
            boatAct.finish();
        }
        catch (ClassNotFoundException  e)
        {
            System.out.println("--- erreur sur la classe = " + e.getMessage());
        }

        return rep.getCode();
    }

}
