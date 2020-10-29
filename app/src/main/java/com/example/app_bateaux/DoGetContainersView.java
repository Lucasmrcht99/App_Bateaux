package com.example.app_bateaux;

import android.content.Intent;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import req_rep_IOBREP.ReponseIOBREP;
import req_rep_IOBREP.RequeteIOBREP;

public class DoGetContainersView extends AsyncTask<Void, Void, Void> {

    private String destination;
    private String mode;
    private ViewContainersAcitivity viewAct;

    public DoGetContainersView(String dest, String mod, ViewContainersAcitivity context)
    {
        destination = dest;
        mode = mod;
        viewAct = context;
    }

    @Override protected void onPreExecute() { super.onPreExecute(); }

    @Override protected void onProgressUpdate(Void... arg1)
    {
        super.onProgressUpdate();
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        ObjectInputStream ois=null;
        ObjectOutputStream oos=null;
        RequeteIOBREP req = new RequeteIOBREP(RequeteIOBREP.GET_CONTAINERS, "");
        Socket cliSock = SocketHandler.getSock();
        try
        {
            req.setChargeUtile(destination + "&" + mode);
            oos = new ObjectOutputStream(cliSock.getOutputStream());
            oos.writeObject(req);
            oos.flush();
        }
        catch (IOException e)
        {
            System.out.println("--- erreur IO = " + e.getMessage());
            AfficheToast.Affiche( "Connexion au serveur perdue", viewAct);
            viewAct.suite = new Intent(viewAct, LoginActivity.class);
            viewAct.startActivity(viewAct.suite);
            viewAct.finish();
            return null;
        }
        // Lecture de la réponse
        ReponseIOBREP rep = null;
        try
        {
            ois = new ObjectInputStream(cliSock.getInputStream());
            rep = (ReponseIOBREP)ois.readObject();

            if(rep.getCode() == ReponseIOBREP.OK)
            {
                viewAct.setListView(rep.getChargeUtile());
            }
            else
            {
                AfficheToast.Affiche(rep.getChargeUtile(), viewAct);
            }
        }
        catch (ClassNotFoundException  e)
        {
            System.out.println("Erreur sur la classe = " + e.getMessage());
        }
        catch (IOException e)
        {
            System.out.println("--- erreur IO = " + e.getMessage());
            AfficheToast.Affiche( "Connexion au serveur perdue", viewAct);
            viewAct.suite = new Intent(viewAct, LoginActivity.class);
            viewAct.startActivity(viewAct.suite);
            viewAct.finish();
        }
        return null ;
    }
}
