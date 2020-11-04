package com.example.app_bateaux;

import android.content.Intent;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import req_rep_IOBREP.ReponseIOBREP;
import req_rep_IOBREP.RequeteIOBREP;

public class DoHandleContainerIn extends AsyncTask<Void, Void, Integer> {

    private UnloadContainersActivity unloadAct;
    private String Id;
    private String Societe;
    private String Poids;

    public DoHandleContainerIn(UnloadContainersActivity context, String id, String soc, String poids)
    {
        unloadAct = context;
        Id = id;
        Societe = soc;
        Poids = poids;
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
        RequeteIOBREP req = new RequeteIOBREP(RequeteIOBREP.HANDLE_CONTAINER_IN, "");
        Socket cliSock = SocketHandler.getSock();
        try
        {
            req.setChargeUtile(Id + "&" + Societe + "&" + Poids);
            oos = new ObjectOutputStream(cliSock.getOutputStream());
            oos.writeObject(req);
            oos.flush();
        }
        catch (IOException e)
        {
            System.out.println("--- erreur IO = " + e.getMessage());
            AfficheToast.Affiche( "Connexion au serveur perdue", unloadAct);
            unloadAct.suite = new Intent(unloadAct, LoginActivity.class);
            unloadAct.startActivity(unloadAct.suite);
            unloadAct.finish();
            return null;
        }
        // Lecture de la réponse
        ReponseIOBREP rep = null;
        try
        {
            ois = new ObjectInputStream(cliSock.getInputStream());
            rep = (ReponseIOBREP)ois.readObject();
            if(rep.getCode()==ReponseIOBREP.OK)
            {
                unloadAct.setCoord(rep.getChargeUtile());
            }
            else
            {
                unloadAct.setCoord("");
            }
        }
        catch (ClassNotFoundException  e)
        {
            System.out.println("Erreur sur la classe = " + e.getMessage());
        }
        catch (IOException e)
        {
            System.out.println("--- erreur IO = " + e.getMessage());
            AfficheToast.Affiche( "Connexion au serveur perdue", unloadAct);
            unloadAct.suite = new Intent(unloadAct, LoginActivity.class);
            unloadAct.startActivity(unloadAct.suite);
            unloadAct.finish();
        }
        return rep.getCode();
    }
}
