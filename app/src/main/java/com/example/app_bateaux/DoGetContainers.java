package com.example.app_bateaux;

import android.content.Intent;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import req_rep_IOBREP.ReponseIOBREP;
import req_rep_IOBREP.RequeteIOBREP;

public class DoGetContainers extends AsyncTask<Void, Void, Void> {

    private String destination;
    private String mode;
    private LoadContainersActivity loadAct;

    public DoGetContainers(String dest, String mod, LoadContainersActivity context)
    {
        destination = dest;
        mode = mod;
        loadAct = context;
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
            AfficheToast.Affiche( "Connexion au serveur perdue", loadAct);
            loadAct.suite = new Intent(loadAct, LoginActivity.class);
            loadAct.startActivity(loadAct.suite);
            loadAct.finish();
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
                loadAct.setListView(rep.getChargeUtile());
            }
            else
            {
                AfficheToast.Affiche(rep.getChargeUtile(), loadAct);
            }
        }
        catch (ClassNotFoundException  e)
        {
            System.out.println("Erreur sur la classe = " + e.getMessage());
        }
        catch (IOException e)
        {
            System.out.println("--- erreur IO = " + e.getMessage());
            AfficheToast.Affiche( "Connexion au serveur perdue", loadAct);
            loadAct.suite = new Intent(loadAct, LoginActivity.class);
            loadAct.startActivity(loadAct.suite);
            loadAct.finish();
        }
        return null ;
    }
}
